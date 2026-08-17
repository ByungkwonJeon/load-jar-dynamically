package com.example.mainapp.filter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MicrometerMetricsWebFilter implements WebFilter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Timer.Sample sample = Timer.start();

        return DataBufferUtils.join(exchange.getRequest().getBody())
            .flatMap(dataBuffer -> {
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                DataBufferUtils.release(dataBuffer);
                String body = new String(bytes, StandardCharsets.UTF_8);

                // Extract header values
                String clientId = exchange.getRequest().getHeaders().getFirst("client-id");
                String idempotencyKey = exchange.getRequest().getHeaders().getFirst("idempotency-key");

                // Extract body fields
                Map<String, String> bodyFields = extractBodyFields(body, "consentId", "consumerId", "institutionId");

                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .body(Flux.just(exchange.getResponse().bufferFactory().wrap(bytes)))
                    .build();

                ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

                return chain.filter(mutatedExchange)
                    .doOnSuccessOrError((signal, ex) -> {
                        Timer.builder("http.server.requests")
                            .description("Track Finicity API performance")
                            .tags("uri", exchange.getRequest().getPath().value(),
                                  "method", exchange.getRequest().getMethodValue(),
                                  "clientId", safe(clientId),
                                  "idempotencyKey", safe(idempotencyKey),
                                  "consentId", safe(bodyFields.get("consentId")),
                                  "consumerId", safe(bodyFields.get("consumerId")),
                                  "institutionId", safe(bodyFields.get("institutionId")),
                                  "status", exchange.getResponse().getStatusCode() != null
                                            ? String.valueOf(exchange.getResponse().getStatusCode().value())
                                            : "UNKNOWN")
                            .register(Metrics.globalRegistry)
                            .record(sample.stop());
                    });
            });
    }

    private Map<String, String> extractBodyFields(String body, String... fields) {
        Map<String, String> fieldValues = new HashMap<>();
        try {
            JsonNode json = objectMapper.readTree(body);
            for (String field : fields) {
                JsonNode node = json.path(field);
                fieldValues.put(field, node.isMissingNode() ? "N/A" : node.asText());
            }
        } catch (Exception e) {
            for (String field : fields) {
                fieldValues.put(field, "parseError");
            }
        }
        return fieldValues;
    }

    private String safe(String value) {
        return value == null ? "N/A" : value;
    }


/**
 * Renders payload bytes as a single-line, log-safe string. Never returns {@code null} and never
 * throws.
 *
 * <p>A GSR/Confluent Avro payload is not "text or binary" — it is binary framing with UTF-8 text
 * embedded <em>between</em> the framing bytes. Avro encodes every string length as a zig-zag
 * varint, so a length below 32 encodes as a byte {@code < 0x20}: a control character sitting in
 * the middle of the record, not just at the front. An all-or-nothing check
 * ({@code CodingErrorAction.REPORT}, or "reject if any control character is present") therefore
 * rejects essentially every Avro record and discards the readable JSON along with it.
 *
 * <p>{@code CodingErrorAction.REPLACE} is not the answer either. That handler only substitutes
 * bytes that are <em>malformed</em>. Bytes {@code 0x00}-{@code 0x1F} and {@code 0x7F} are valid
 * UTF-8, so a decoder passes them straight through and they land in the log file as raw control
 * characters — precisely the garbling being fixed here.
 *
 * <p>So this method classifies per byte. A position that starts a valid, printable UTF-8 sequence
 * is emitted as text (multi-byte characters — Hangul, CJK, emoji — are preserved intact); anything
 * else consumes exactly one byte and is counted into a {@code [~n]} run marker. {@code \t \n \r}
 * are escaped, so one record stays on one log line and downstream parsers (Splunk/ELK) do not
 * split it.
 *
 * <p><b>Limitation:</b> a framing byte whose value happens to fall in printable ASCII is
 * indistinguishable from real text and still appears. In the sample log the {@code H} in
 * {@code ApiResponseSentH48f02b58-...} is {@code 0x48} — a zig-zag varint length (72 &rarr; 36,
 * the length of the UUID that follows), not a character. No byte-level heuristic can remove it.
 * For byte-exact output, strip the framing and deserialize with the Avro schema instead.
 *
 * @param bytes the raw payload; may be {@code null}
 * @return log-safe text with binary runs collapsed to {@code [~n]} markers
 */
private static String renderForLog(byte[] bytes) {
    if (bytes == null) {
        return "null";
    }
    if (bytes.length == 0) {
        return "";
    }
    int limit = Math.min(bytes.length, MAX_DECODE_BYTES);
    StringBuilder sb = new StringBuilder(limit + 48);

    int gap = 0;
    int i = 0;
    while (i < limit) {
        int len = printableSequenceLength(bytes, i, limit);
        if (len == 0) {
            gap++;
            i++;                                        // binary: consume exactly one byte
            continue;
        }
        if (gap > 0) {
            sb.append("[~").append(gap).append(']');
            gap = 0;
        }
        int b0 = bytes[i] & 0xFF;
        if (b0 == '\n') {
            sb.append("\\n");
        } else if (b0 == '\r') {
            sb.append("\\r");
        } else if (b0 == '\t') {
            sb.append("\\t");
        } else {
            sb.append(new String(bytes, i, len, StandardCharsets.UTF_8));
        }
        i += len;
    }
    if (gap > 0) {
        sb.append("[~").append(gap).append(']');
    }
    if (bytes.length > limit) {
        sb.append("...[truncated; byte[len=").append(bytes.length)
          .append("],rendered=").append(limit).append(']');
    }
    return sb.toString();
}

/**
 * Returns the length in bytes of the printable UTF-8 sequence starting at {@code i}, or {@code 0}
 * if this position does not start one. Rejects control characters (except {@code \t \n \r}), DEL,
 * bare continuation bytes, overlong encodings, UTF-16 surrogate halves, code points above
 * U+10FFFF, and sequences that would run past {@code end}.
 */
private static int printableSequenceLength(byte[] b, int i, int end) {
    int b0 = b[i] & 0xFF;

    if (b0 == 0x09 || b0 == 0x0A || b0 == 0x0D) {
        return 1;                                       // tab / LF / CR — escaped by the caller
    }
    if (b0 >= 0x20 && b0 <= 0x7E) {
        return 1;                                       // printable ASCII
    }
    if (b0 < 0xC2 || b0 > 0xF4) {
        return 0;                                       // control char, DEL, continuation byte,
    }                                                   // or an overlong/invalid lead byte
    int continuations = (b0 <= 0xDF) ? 1 : (b0 <= 0xEF) ? 2 : 3;
    if (i + continuations >= end) {
        return 0;                                       // sequence cut off by the decode window
    }
    for (int k = 1; k <= continuations; k++) {
        int bx = b[i + k] & 0xFF;
        if (bx < 0x80 || bx > 0xBF) {
            return 0;                                   // not a continuation byte
        }
    }
    int b1 = b[i + 1] & 0xFF;
    if (b0 == 0xE0 && b1 < 0xA0) {
        return 0;                                       // overlong 3-byte form
    }
    if (b0 == 0xED && b1 >= 0xA0) {
        return 0;                                       // UTF-16 surrogate half (U+D800..U+DFFF)
    }
    if (b0 == 0xF0 && b1 < 0x90) {
        return 0;                                       // overlong 4-byte form
    }
    if (b0 == 0xF4 && b1 > 0x8F) {
        return 0;                                       // beyond U+10FFFF
    }
    return continuations + 1;
}

}
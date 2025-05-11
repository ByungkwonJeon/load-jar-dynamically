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
}
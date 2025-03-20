package com.example.mainapp.sample;

import org.springframework.stereotype.Service;

@Service
public class ResponseCodeParser {

    public String parseResponseCode(String responseCode) {
        if (responseCode == null || responseCode.length() != 4) {
            return "Invalid response code format";
        }

        int statusCode = Integer.parseInt(responseCode.substring(0, 2));
        int providerCode = Integer.parseInt(responseCode.substring(2, 4));

        TransactionStatus status = TransactionStatus.fromCode(statusCode);
        Provider provider = Provider.fromCode(providerCode);

        return String.format("Status: %s (%s) | Provider: %s",
                status.getDescription(), status.getType(), provider.getDescription());
    }
}
package com.alfa.file_uploader.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Component
public class StatusClient {

    private final RestTemplate restTemplate;

    @Autowired
    public StatusClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getStatus(String hash) {
        try {
            ResponseEntity<String> response =
                    restTemplate.getForEntity("http://file-status-processor:8080/status/{hash}",
                            String.class,
                            hash);
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }
}

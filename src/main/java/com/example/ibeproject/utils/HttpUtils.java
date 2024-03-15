package com.example.ibeproject.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class HttpUtils {

    private HttpUtils() {
        
    }

    public static HttpHeaders createHttpHeaders(String apiKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", apiKey);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    public static ResponseEntity<String> makeHttpRequest(
        RestTemplate restTemplate, 
        String requestBody, 
        HttpHeaders headers,
        String graphqlServerUrl
    ) {
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        return restTemplate.exchange(graphqlServerUrl, HttpMethod.POST, requestEntity, String.class);
    }
}

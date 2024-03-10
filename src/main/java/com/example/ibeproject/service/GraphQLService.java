package com.example.ibeproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Service
public class GraphQLService {
    private static final String ROOMS_DATA = "{ listRooms {room_id room_number } }";

    @Value("${graphql.connection.key}")
    private static String apiKey;

    @Value("${graphql.server.url}")
    private static String graphqlServerUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public GraphQLService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> getRooms() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("x-api-key", apiKey);
        String requestBody = "{ \"query\": \"" + ROOMS_DATA + "\" }";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, httpHeaders);
        try {
            return restTemplate.exchange(graphqlServerUrl, HttpMethod.POST, requestEntity, String.class);
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RuntimeException("Error during GraphQL request", e);
        }
    }
}

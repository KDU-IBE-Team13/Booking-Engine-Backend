package com.example.ibeproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Service
public class GraphQLService {
    private static final String ROOMS_DATA = "{ listRooms {room_id room_number } }";
    private static final String APIKEY = "da2-fakeApiId123456";

    private final RestTemplate restTemplate;
    @Autowired
    public GraphQLService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

        public ResponseEntity<String> getRooms(){
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.set("x-api-key", APIKEY);
            String requestBody = "{ \"query\": \"" + ROOMS_DATA + "\" }";
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, httpHeaders);
            try {
                return restTemplate.exchange("http://localhost:4000/graphql", HttpMethod.POST, requestEntity, String.class);
            } catch (RestClientException e) {
                e.printStackTrace();
                throw new RuntimeException("Error during GraphQL request", e);
            }
    }
}

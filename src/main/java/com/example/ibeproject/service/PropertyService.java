package com.example.ibeproject.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.ibeproject.constants.GraphQLConstants;
import com.example.ibeproject.exceptions.PropertyException;

@Service
public class PropertyService {

    String listPropertiesQuery = GraphQLConstants.LIST_PROPERTIES_QUERY_STRING;

    @Value("${graphql.connection.key}")
    private String apiKey;

    @Value("${graphql.server.url}")
    private String graphqlServerUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public PropertyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> getAllProperties() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("x-api-key", apiKey);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        String requestBody = "{ \"query\": \"" + listPropertiesQuery + "\" }";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, httpHeaders);
        try {
            return restTemplate.exchange(graphqlServerUrl, HttpMethod.POST, requestEntity, String.class);
        } catch (RestClientException e) {
            throw new PropertyException("Error fetching properties: ", e);
        }
    }

}

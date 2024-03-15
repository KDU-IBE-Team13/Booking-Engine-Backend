package com.example.ibeproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.ibeproject.constants.GraphQLConstants;
import com.example.ibeproject.exceptions.PropertyException;
import com.example.ibeproject.utils.GraphQLRequestBodyUtils;
import com.example.ibeproject.utils.HttpUtils;

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

    /**
     * Retrieves all properties.
     *
     * @return A string containing the response body with all properties.
     * @throws PropertyException If an error occurs while fetching properties.
     */
    public String getAllProperties() {
        HttpHeaders headers = HttpUtils.createHttpHeaders(apiKey);
        String requestBody = GraphQLRequestBodyUtils.buildQueryRequestBody(listPropertiesQuery);

        try {
            ResponseEntity<String> responseEntity = HttpUtils.makeHttpRequest(restTemplate, requestBody, headers, graphqlServerUrl);
            return responseEntity.getBody();
        } catch (RestClientException e) {
            throw new PropertyException("Error fetching properties: ", e);
        }
    }

}

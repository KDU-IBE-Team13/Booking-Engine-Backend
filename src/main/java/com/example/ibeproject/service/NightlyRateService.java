package com.example.ibeproject.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.ibeproject.constants.GraphQLConstants;
import com.example.ibeproject.exceptions.NightlyRateException;
import com.example.ibeproject.utils.DateUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NightlyRateService {

    String nightlyRatesQuery = GraphQLConstants.LIST_NIGHTLY_RATES_QUERY_STRING;

    @Value("${graphql.connection.key}")
    private String apiKey;

    @Value("${graphql.server.url}")
    private String graphqlServerUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public NightlyRateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<Map<String, Double>> getMinimumNightlyRates(int page, int pageSize) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("x-api-key", apiKey);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        
        String requestBody = "{ \"query\": \"" + nightlyRatesQuery + "\" }";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, httpHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(graphqlServerUrl, HttpMethod.POST, requestEntity, String.class);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode responseNode = mapper.readTree(responseEntity.getBody());
            JsonNode roomTypeNode = responseNode.at("/data/getProperty/room_type");

            Map<String, List<Double>> ratesByDate = new HashMap<>();

            for (JsonNode room : roomTypeNode) {
                JsonNode roomRatesNode = room.get("room_rates");
                for (JsonNode rateNode : roomRatesNode) {
                    JsonNode rate = rateNode.get("room_rate");
                    String date = rate.get("date").asText();
                    double basicNightlyRate = rate.get("basic_nightly_rate").asDouble();
                    
                    ratesByDate.computeIfAbsent(date, k -> new ArrayList<>()).add(basicNightlyRate);
                }
            }

            List<String> allDates = new ArrayList<>(ratesByDate.keySet());
            for (int i = 0; i < 9; i++) {
                for (String date : allDates) {
                    ratesByDate.put(incrementDate(date, i), ratesByDate.get(date));
                }
            }

            Map<String, Double> minRatesByDate = new HashMap<>();
            for (Map.Entry<String, List<Double>> entry : ratesByDate.entrySet()) {
                double minRate = entry.getValue().stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
                minRatesByDate.put(entry.getKey(), minRate);
            }

            Map<String, Double> sortedMinRatesByDate = minRatesByDate.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            int startIndex = (page - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, sortedMinRatesByDate.size());

            Map<String, Double> paginatedMinRatesByDate = sortedMinRatesByDate.entrySet()
                    .stream()
                    .skip(startIndex)
                    .limit(endIndex - startIndex)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            Map<String, Double> sortedPaginatedMinRatesByDate = paginatedMinRatesByDate.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));


            return ResponseEntity.ok(sortedPaginatedMinRatesByDate);
        } catch (Exception e) {
            throw new NightlyRateException("Error fetching nightly rates: ", e);
        }
    }

    private String incrementDate(String dateTimeString, int months) {
        return DateUtils.incrementDate(dateTimeString, months);
    }

}

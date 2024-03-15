package com.example.ibeproject.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.ibeproject.constants.GraphQLConstants;
import com.example.ibeproject.exceptions.NightlyRateException;
import com.example.ibeproject.exceptions.NightlyRateParsingException;
import com.example.ibeproject.utils.DateUtils;
import com.example.ibeproject.utils.GraphQLRequestBodyUtils;
import com.example.ibeproject.utils.HttpUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    /**
     * Retrieves minimum nightly rates.
     *
     * @param page     Page number.
     * @param pageSize Number of items per page.
     * @return A map containing dates and their corresponding minimum nightly rates.
     * @throws NightlyRateException If an error occurs while fetching nightly rates.
     */
    public Map<String, Double> getMinimumNightlyRates(int page, int pageSize) {
        String requestBody = GraphQLRequestBodyUtils.buildQueryRequestBody(nightlyRatesQuery);
        HttpHeaders headers = HttpUtils.createHttpHeaders(apiKey);

        try {
            ResponseEntity<String> responseEntity = HttpUtils.makeHttpRequest(restTemplate, requestBody, headers, graphqlServerUrl);
            return parseNightlyRatesResponse(responseEntity.getBody(), page, pageSize);
        } catch (RestClientException e) {
            throw new NightlyRateException("Error fetching nightly rates: ", e);
        }
    }

    private Map<String, Double> parseNightlyRatesResponse(String responseBody, int page, int pageSize) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode responseNode = objectMapper.readTree(responseBody).at("/data/getProperty/room_type");
            Map<String, List<Double>> ratesByDate = extractRatesByDate(responseNode);
            Map<String, Double> minRatesByDate = calculateMinRatesByDate(ratesByDate);
            Map<String, Double> sortedMinRatesByDate = sortMapByKey(minRatesByDate);
            return paginateMap(sortedMinRatesByDate, page, pageSize);
        } catch (JsonProcessingException e) {
            throw new NightlyRateParsingException("Error processing JSON", e);
        }
    }
    
    private Map<String, List<Double>> extractRatesByDate(JsonNode responseNode) {
        Map<String, List<Double>> ratesByDate = new HashMap<>();
        for (JsonNode room : responseNode) {
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
        return ratesByDate;
    }
    
    private Map<String, Double> calculateMinRatesByDate(Map<String, List<Double>> ratesByDate) {
        Map<String, Double> minRatesByDate = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : ratesByDate.entrySet()) {
            double minRate = entry.getValue().stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
            minRatesByDate.put(entry.getKey(), minRate);
        }
        return minRatesByDate;
    }
    
    private Map<String, Double> sortMapByKey(Map<String, Double> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
    
    private Map<String, Double> paginateMap(Map<String, Double> map, int page, int pageSize) {
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, map.size());
        return map.entrySet()
                .stream()
                .skip(startIndex)
                .limit(endIndex - startIndex)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
    

    private String incrementDate(String dateTimeString, int months) {
        return DateUtils.incrementDate(dateTimeString, months);
    }

}

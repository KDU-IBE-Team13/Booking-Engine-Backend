package com.example.ibeproject.utils;

public class GraphQLRequestBodyUtils {

    private GraphQLRequestBodyUtils() {
        
    }

    public static String buildQueryRequestBody(String query) {
        return "{ \"query\": \"" + query + "\" }";
    }
}

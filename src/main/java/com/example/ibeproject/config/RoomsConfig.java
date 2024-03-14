package com.example.ibeproject.config;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomsConfig {
    @JsonProperty("enabled")
    private boolean enabled;

    @JsonProperty("options")
    private List<Integer> options;

}
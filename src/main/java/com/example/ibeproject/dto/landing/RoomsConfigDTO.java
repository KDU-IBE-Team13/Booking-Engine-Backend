package com.example.ibeproject.dto.landing;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomsConfigDTO {
    @JsonProperty("enabled")
    private boolean enabled;

    @JsonProperty("options")
    private List<Integer> options;

}
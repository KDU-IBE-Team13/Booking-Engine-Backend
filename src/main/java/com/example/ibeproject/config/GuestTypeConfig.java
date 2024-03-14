package com.example.ibeproject.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestTypeConfig {
    @JsonProperty("type")
    private String type;

    @JsonProperty("maxCount")
    private int maxCount;

    @JsonProperty("ageRange")
    private String ageRange;

    @JsonProperty("enabled")
    private boolean enabled;

}
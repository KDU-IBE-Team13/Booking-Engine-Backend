package com.example.ibeproject.dto.landing;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestTypeConfigDTO {
    @JsonProperty("type")
    private String type;

    @JsonProperty("maxCount")
    private int maxCount;

    @JsonProperty("ageRange")
    private String ageRange;

    @JsonProperty("enabled")
    private boolean enabled;

}
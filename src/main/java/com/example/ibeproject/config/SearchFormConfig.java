package com.example.ibeproject.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchFormConfig {
    @JsonProperty("lengthOfStay")
    private int lengthOfStay;

    @JsonProperty("guests")
    private GuestsConfig guestsConfig;

    @JsonProperty("rooms")
    private RoomsConfig roomsConfig;

    @JsonProperty("wheelchairAccessible")
    private boolean wheelchairAccessible;
}

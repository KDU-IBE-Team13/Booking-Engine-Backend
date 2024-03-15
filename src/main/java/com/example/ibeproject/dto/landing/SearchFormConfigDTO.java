package com.example.ibeproject.dto.landing;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchFormConfigDTO {

    @JsonProperty("lengthOfStay")
    private int lengthOfStay;

    @JsonProperty("guests")
    private GuestsConfigDTO guestsConfig;

    @JsonProperty("rooms")
    private RoomsConfigDTO roomsConfig;

    @JsonProperty("wheelchairAccessible")
    private boolean wheelchairAccessible;
}

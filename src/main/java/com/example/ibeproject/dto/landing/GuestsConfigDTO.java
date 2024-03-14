package com.example.ibeproject.dto.landing;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestsConfigDTO {
    @JsonProperty("enabled")
    private boolean enabled;

    @JsonProperty("maxGuests")
    private int maxGuests;

    @JsonProperty("guestTypes")
    private List<GuestTypeConfigDTO> guestTypes;

}
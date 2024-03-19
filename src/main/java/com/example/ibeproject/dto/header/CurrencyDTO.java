package com.example.ibeproject.dto.header;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDTO {
    @JsonProperty("currency")
    private String currency;

    @JsonProperty("currencySymbol")
    private String currencySymbol;
}

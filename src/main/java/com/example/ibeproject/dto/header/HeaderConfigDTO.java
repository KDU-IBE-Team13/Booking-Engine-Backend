package com.example.ibeproject.dto.header;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeaderConfigDTO {
    @JsonProperty("logo")
    private String logo;

    @JsonProperty("title")
    private String title;

    @JsonProperty("supportedLanguages")
    private List<LanguageDTO> supportedLanguages;

    @JsonProperty("supportedCurrencies")
    private List<CurrencyDTO> supportedCurrencies;
}
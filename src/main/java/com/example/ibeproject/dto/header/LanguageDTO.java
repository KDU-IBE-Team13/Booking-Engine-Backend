package com.example.ibeproject.dto.header;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LanguageDTO {
    @JsonProperty("key")
    private String key;

    @JsonProperty("langName")
    private String langName;
}

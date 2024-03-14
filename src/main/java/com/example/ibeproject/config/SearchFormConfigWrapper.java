package com.example.ibeproject.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchFormConfigWrapper {
    @JsonProperty("searchForm")
    private SearchFormConfig searchFormConfig;
}



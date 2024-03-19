package com.example.ibeproject.dto.landing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LandingConfigDTO {
    @JsonProperty("bannerImageURL")
    private String bannerImageURL;

    @JsonProperty("searchForm")
    private SearchFormConfigDTO searchFormConfig;
}



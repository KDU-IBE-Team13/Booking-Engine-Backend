package com.example.ibeproject.dto.footer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FooterConfigDTO {
    @JsonProperty("logo")
    private String logo;

    @JsonProperty("companyName")
    private String companyName;
}

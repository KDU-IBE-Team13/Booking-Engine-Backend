package com.example.ibeproject.dto.nightlyrate;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NightlyRateResponseDTO {
    private Map<String, Double> nightlyRates;
}

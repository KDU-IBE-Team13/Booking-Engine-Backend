package com.example.ibeproject.controller;

import com.example.ibeproject.dto.nightlyrate.NightlyRateResponseDTO;
import com.example.ibeproject.service.NightlyRateService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/nightly-rate")
public class NightlyRateController {
    private final NightlyRateService nightlyRateService;

    /**
     * @param nightlyRateService The service for managing nightly rates.
     */
    @Autowired
    public NightlyRateController(NightlyRateService nightlyRateService) {
        this.nightlyRateService = nightlyRateService;
    }

    /**
     * Retrieves nightly rates data.
     * @param page The page number for pagination.
     * @param pageSize The size of each page for pagination.
     * @return ResponseEntity containing the nightly rates data.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NightlyRateResponseDTO> getAllNightlyRates(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "61") int pageSize) {
        Map<String, Double> nightlyRates = nightlyRateService.getMinimumNightlyRates(page, pageSize);
        NightlyRateResponseDTO responseDTO = new NightlyRateResponseDTO();
        responseDTO.setNightlyRates(nightlyRates);
        return ResponseEntity.ok(responseDTO);
    }
}

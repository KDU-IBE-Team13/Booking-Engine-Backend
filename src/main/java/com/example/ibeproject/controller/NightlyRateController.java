package com.example.ibeproject.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ibeproject.service.NightlyRateService;


@RestController
@RequestMapping("/api/v1/nightly-rate")
public class NightlyRateController {
    private NightlyRateService roomRateService;

    @Autowired
    public NightlyRateController(NightlyRateService roomRateService) {
        this.roomRateService = roomRateService;
    }

    @CrossOrigin
    @GetMapping
    public ResponseEntity<Map<String, Double>> getAllNightlyRates(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "61") int pageSize) {
        return roomRateService.getMinimumNightlyRates(page, pageSize);
    }

}

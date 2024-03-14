package com.example.ibeproject.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ibeproject.config.ConfigService;
import com.example.ibeproject.config.SearchFormConfig;

@RestController
@RequestMapping("/api/v1/configuration")
public class ConfigurationController {

    private final ConfigService configService;
    private final String configFilePath;

    public ConfigurationController(ConfigService configService, @Value("${config.file.path}") String configFilePath) {
        this.configService = configService;
        this.configFilePath = configFilePath;
    }

    @GetMapping("/landing-page")
    public ResponseEntity<SearchFormConfig> getLandingPageConfig() {
        try {
            SearchFormConfig config = configService.loadConfigFromFile(configFilePath);
            return ResponseEntity.ok(config);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

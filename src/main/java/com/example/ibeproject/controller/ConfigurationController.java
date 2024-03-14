package com.example.ibeproject.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ibeproject.dto.footer.FooterConfigDTO;
import com.example.ibeproject.dto.header.HeaderConfigDTO;
import com.example.ibeproject.dto.landing.SearchFormConfigDTO;
import com.example.ibeproject.exceptions.ConfigLoadException;
import com.example.ibeproject.service.FooterConfigService;
import com.example.ibeproject.service.HeaderConfigService;
import com.example.ibeproject.service.LandingConfigService;

@RestController
@RequestMapping("/api/v1/configuration")
public class ConfigurationController {

    private final HeaderConfigService headerConfigService;
    private final FooterConfigService footerConfigService;
    private final LandingConfigService landingConfigService;
    private final String headerConfigAzureFilePath;
    private final String footerConfigAzureFilePath;
    private final String landingConfigAzureFilePath;


    public ConfigurationController(
        HeaderConfigService headerConfigService,
        FooterConfigService footerConfigService,
        LandingConfigService landingConfigService,
        @Value("${config.header.azure.file.path}") String headerConfigAzureFilePath,
        @Value("${config.footer.azure.file.path}") String footerConfigAzureFilePath,
        @Value("${config.landing.azure.file.path}") String landingConfigAzureFilePath
    ) {
        this.headerConfigService = headerConfigService;
        this.footerConfigService = footerConfigService;
        this.landingConfigService = landingConfigService;
        this.headerConfigAzureFilePath = headerConfigAzureFilePath;
        this.footerConfigAzureFilePath = footerConfigAzureFilePath;
        this.landingConfigAzureFilePath = landingConfigAzureFilePath;
    }

    @GetMapping("/header")
    public ResponseEntity<HeaderConfigDTO> getHeaderConfig() {
        try {
            HeaderConfigDTO config = headerConfigService.loadConfigFromAzureBlob(headerConfigAzureFilePath);
            return ResponseEntity.ok(config);
        } catch (IOException e) {
            throw new ConfigLoadException("Failed to load header configuration", e);
        }
    }

    @GetMapping("/footer")
    public ResponseEntity<FooterConfigDTO> getFooterConfig() {
        try {
            FooterConfigDTO config = footerConfigService.loadConfigFromAzureBlob(footerConfigAzureFilePath);
            return ResponseEntity.ok(config);
        } catch (IOException e) {
            throw new ConfigLoadException("Failed to load footer configuration", e);
        }
    }

    @GetMapping(value = "/landing-page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SearchFormConfigDTO> getLandingPageConfig() {
        try {
            SearchFormConfigDTO config = landingConfigService.loadConfigFromAzureBlob(landingConfigAzureFilePath);
            return ResponseEntity.ok(config);
        } catch (IOException e) {
            throw new ConfigLoadException("Failed to load landing page config", e);
        }
    }
}

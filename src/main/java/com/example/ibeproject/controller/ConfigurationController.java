package com.example.ibeproject.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ibeproject.dto.footer.FooterConfigDTO;
import com.example.ibeproject.dto.header.HeaderConfigDTO;
import com.example.ibeproject.dto.landing.LandingConfigDTO;
import com.example.ibeproject.dto.landing.SearchFormConfigDTO;
import com.example.ibeproject.exceptions.ConfigLoadException;
import com.example.ibeproject.service.FooterConfigService;
import com.example.ibeproject.service.HeaderConfigService;
import com.example.ibeproject.service.LandingConfigService;

@CrossOrigin
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

    @CrossOrigin
    @GetMapping(value = "/header", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HeaderConfigDTO> getHeaderConfig() {
        try {
            HeaderConfigDTO config = headerConfigService.loadConfigFromAzureBlob(headerConfigAzureFilePath);
            return ResponseEntity.ok(config);
        } catch (IOException e) {
            throw new ConfigLoadException("Failed to load header configuration", e);
        }
    }

    @PostMapping("/header")
    public ResponseEntity<String> updateHeaderConfig(@RequestBody HeaderConfigDTO updatedConfig) {
        try {
            headerConfigService.writeConfigToAzureBlob(updatedConfig, headerConfigAzureFilePath);
            return ResponseEntity.ok("Header config updated successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update header config: " + e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping(value = "/footer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FooterConfigDTO> getFooterConfig() {
        try {
            FooterConfigDTO config = footerConfigService.loadConfigFromAzureBlob(footerConfigAzureFilePath);
            return ResponseEntity.ok(config);
        } catch (IOException e) {
            throw new ConfigLoadException("Failed to load footer configuration", e);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/landing-page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LandingConfigDTO> getLandingPageConfig() {
        try {
            LandingConfigDTO config = landingConfigService.loadConfigFromAzureBlob(landingConfigAzureFilePath);
            return ResponseEntity.ok(config);
        } catch (IOException e) {
            throw new ConfigLoadException("Failed to load landing page config", e);
        }
    }


    @PostMapping("/landing-page")
    public ResponseEntity<String> updateLandingPageConfig(@RequestBody LandingConfigDTO updatedConfig) {
        try {
            landingConfigService.writeConfigToAzureBlob(updatedConfig, landingConfigAzureFilePath);
            return ResponseEntity.ok("Landing page config updated successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update landing page config: " + e.getMessage());
        }
    }

}

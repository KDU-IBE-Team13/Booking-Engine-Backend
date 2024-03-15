package com.example.ibeproject.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.example.ibeproject.dto.header.HeaderConfigDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class HeaderConfigService {
    private final ObjectMapper objectMapper;
    private final String azureBlobConnectionString;
    private final String containerName;

    public HeaderConfigService(
            ObjectMapper objectMapper,
            @Value("${config.azure.blob.connection.string}") String azureBlobConnectionString,
            @Value("${config.azure.blob.container.name}") String containerName
    ) {
        this.objectMapper = objectMapper;
        this.azureBlobConnectionString = azureBlobConnectionString;
        this.containerName = containerName;
    }

    public void writeConfigToAzureBlob(HeaderConfigDTO updatedConfig, String blobName) throws IOException {
        BlobServiceClientBuilder clientBuilder = new BlobServiceClientBuilder().connectionString(azureBlobConnectionString);
        BlobClient blobClient = clientBuilder.buildClient().getBlobContainerClient(containerName).getBlobClient(blobName);
    
        HeaderConfigDTO existingConfig = loadConfigFromAzureBlob(blobName);
    
        if (updatedConfig.getLogo() != null) {
            existingConfig.setLogo(updatedConfig.getLogo());
        }
        // if (updatedConfig.getTitle() != null) {
        //     existingConfig.setTitle(updatedConfig.getTitle());
        // }
        if (updatedConfig.getSupportedLanguages() != null && !updatedConfig.getSupportedLanguages().isEmpty()) {
            existingConfig.setSupportedLanguages(updatedConfig.getSupportedLanguages());
        }
        if (updatedConfig.getSupportedCurrencies() != null && !updatedConfig.getSupportedCurrencies().isEmpty()) {
            existingConfig.setSupportedCurrencies(updatedConfig.getSupportedCurrencies());
        }
    
        String jsonString = objectMapper.writeValueAsString(existingConfig);
        
        InputStream dataStream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
        
        blobClient.upload(dataStream, jsonString.length(), true);
    }
    

    public HeaderConfigDTO loadConfigFromAzureBlob(String blobName) throws IOException {
        BlobServiceClientBuilder clientBuilder = new BlobServiceClientBuilder().connectionString(azureBlobConnectionString);
        BlobClient blobClient = clientBuilder.buildClient().getBlobContainerClient(containerName).getBlobClient(blobName);

        byte[] data = blobClient.downloadContent().toBytes();
        return objectMapper.readValue(new ByteArrayInputStream(data), HeaderConfigDTO.class);
    }
}

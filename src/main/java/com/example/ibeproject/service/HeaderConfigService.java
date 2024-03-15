package com.example.ibeproject.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.example.ibeproject.dto.header.HeaderConfigDTO;
import com.example.ibeproject.exceptions.ConfigLoadException;
import com.example.ibeproject.exceptions.ConfigUpdateException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
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

    /**
     * Writes the updated header configuration to Azure Blob Storage.
     * @param updatedConfig Updated header configuration.
     * @param blobName Name of the blob where the configuration will be stored.
     * @throws ConfigUpdateException if there is an error while updating the configuration.
     */
    public void writeConfigToAzureBlob(HeaderConfigDTO updatedConfig, String blobName) {
        try {
            BlobServiceClientBuilder clientBuilder = new BlobServiceClientBuilder().connectionString(azureBlobConnectionString);
            BlobClient blobClient = clientBuilder.buildClient().getBlobContainerClient(containerName).getBlobClient(blobName);
        
            HeaderConfigDTO existingConfig = loadConfigFromAzureBlob(blobName);
        
            if (updatedConfig.getLogo() != null) {
                existingConfig.setLogo(updatedConfig.getLogo());
            }
            if (updatedConfig.getTitle() != null) {
                existingConfig.setTitle(updatedConfig.getTitle());
            }
            if (updatedConfig.getSupportedLanguages() != null && !updatedConfig.getSupportedLanguages().isEmpty()) {
                existingConfig.setSupportedLanguages(updatedConfig.getSupportedLanguages());
            }
            if (updatedConfig.getSupportedCurrencies() != null && !updatedConfig.getSupportedCurrencies().isEmpty()) {
                existingConfig.setSupportedCurrencies(updatedConfig.getSupportedCurrencies());
            }
        
            String jsonString = objectMapper.writeValueAsString(existingConfig);
            
            InputStream dataStream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
            
            blobClient.upload(dataStream, jsonString.length(), true);
        } catch (IOException e) {
            throw new ConfigUpdateException("Failed to write header configuration to Azure Blob", e);
        }
    }
    
    /**
     * Loads the header configuration from Azure Blob Storage.
     * @param blobName Name of the blob where the configuration is stored.
     * @return HeaderConfigDTO representing the loaded header configuration.
     * @throws ConfigLoadException if there is an error while loading the configuration.
     */
    public HeaderConfigDTO loadConfigFromAzureBlob(String blobName) {
        try {
            BlobServiceClientBuilder clientBuilder = new BlobServiceClientBuilder().connectionString(azureBlobConnectionString);
            BlobClient blobClient = clientBuilder.buildClient().getBlobContainerClient(containerName).getBlobClient(blobName);

            byte[] data = blobClient.downloadContent().toBytes();
            return objectMapper.readValue(new ByteArrayInputStream(data), HeaderConfigDTO.class);
        } catch (IOException e) {
            throw new ConfigLoadException("Failed to load header configuration from Azure Blob", e);
        }
    }
}

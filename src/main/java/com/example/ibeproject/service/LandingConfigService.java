package com.example.ibeproject.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.example.ibeproject.dto.landing.LandingConfigDTO;
import com.example.ibeproject.dto.landing.SearchFormConfigDTO;
import com.example.ibeproject.exceptions.ConfigLoadException;
import com.example.ibeproject.exceptions.ConfigUpdateException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class LandingConfigService {
    private final ObjectMapper objectMapper;
    private final String azureBlobConnectionString;
    private final String containerName;

    public LandingConfigService(
            ObjectMapper objectMapper,
            @Value("${config.azure.blob.connection.string}") String azureBlobConnectionString,
            @Value("${config.azure.blob.container.name}") String containerName
    ) {
        this.objectMapper = objectMapper;
        this.azureBlobConnectionString = azureBlobConnectionString;
        this.containerName = containerName;
    }

    /**
     * Loads landing page configuration from Azure Blob Storage.
     *
     * @param blobName Name of the blob containing the configuration.
     * @return LandingConfigDTO representing the landing page configuration.
     * @throws ConfigLoadException If failed to load landing page config from Azure Blob Storage.
     */
    public LandingConfigDTO loadConfigFromAzureBlob(String blobName) {
        try {
            BlobServiceClientBuilder clientBuilder = new BlobServiceClientBuilder().connectionString(azureBlobConnectionString);
            BlobClient blobClient = clientBuilder.buildClient().getBlobContainerClient(containerName).getBlobClient(blobName);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            blobClient.downloadStream(outputStream);

            byte[] data = outputStream.toByteArray();
            return objectMapper.readValue(data, LandingConfigDTO.class);
        } catch (IOException e) {
            throw new ConfigUpdateException("Failed to load landing page config from Azure Blob", e);
        }
    }

    /**
     * Writes updated landing page configuration to Azure Blob Storage.
     *
     * @param updatedConfig Updated landing page configuration.
     * @param blobName      Name of the blob to which the configuration will be written.
     * @throws ConfigLoadException If failed to write landing page config to Azure Blob Storage.
     */
    public void writeConfigToAzureBlob(LandingConfigDTO updatedConfig, String blobName) {
        try {
            BlobServiceClientBuilder clientBuilder = new BlobServiceClientBuilder().connectionString(azureBlobConnectionString);
            BlobClient blobClient = clientBuilder.buildClient().getBlobContainerClient(containerName).getBlobClient(blobName);

            LandingConfigDTO existingConfig = loadConfigFromAzureBlob(blobName);

            if (updatedConfig.getBannerImageURL() != null) {
                existingConfig.setBannerImageURL(updatedConfig.getBannerImageURL());
            }
        
            SearchFormConfigDTO existingSearchFormConfig = new SearchFormConfigDTO();

            if (updatedConfig.getSearchFormConfig() != null) {
                SearchFormConfigDTO updatedSearchFormConfig = updatedConfig.getSearchFormConfig();
        
                if (updatedSearchFormConfig.getLengthOfStay() != 0) {
                    existingSearchFormConfig.setLengthOfStay(updatedSearchFormConfig.getLengthOfStay());
                }
        
                if (updatedSearchFormConfig.getGuestsConfig() != null) {
                    existingSearchFormConfig.setGuestsConfig(updatedSearchFormConfig.getGuestsConfig());
                }
        
                if (updatedSearchFormConfig.getRoomsConfig() != null) {
                    existingSearchFormConfig.setRoomsConfig(updatedSearchFormConfig.getRoomsConfig());
                }
        
                existingSearchFormConfig.setWheelchairAccessible(updatedSearchFormConfig.isWheelchairAccessible());
            }
        
            existingConfig.setSearchFormConfig(existingSearchFormConfig);

            String jsonString = objectMapper.writeValueAsString(existingConfig);

            InputStream dataStream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));

            blobClient.upload(dataStream, jsonString.length(), true);
        } catch (IOException e) {
            throw new ConfigLoadException("Failed to write landing page config to Azure Blob", e);
        }
    }
}

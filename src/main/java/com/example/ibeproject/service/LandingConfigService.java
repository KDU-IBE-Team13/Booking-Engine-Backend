package com.example.ibeproject.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.example.ibeproject.dto.landing.SearchFormConfigDTO;
import com.example.ibeproject.dto.landing.LandingConfigDTO;
import com.example.ibeproject.dto.landing.GuestsConfigDTO;
import com.example.ibeproject.dto.landing.RoomsConfigDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    // public SearchFormConfigDTO loadConfigFromAzureBlob(String blobName) throws IOException {
    //     BlobServiceClientBuilder clientBuilder = new BlobServiceClientBuilder().connectionString(azureBlobConnectionString);
    //     BlobClient blobClient = clientBuilder.buildClient().getBlobContainerClient(containerName).getBlobClient(blobName);

    //     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    //     blobClient.downloadStream(outputStream);

    //     byte[] data = outputStream.toByteArray();
    //     return objectMapper.readValue(data, SearchFormConfigDTO.class);
    // }

    public LandingConfigDTO loadConfigFromAzureBlob(String blobName) throws IOException {
        BlobServiceClientBuilder clientBuilder = new BlobServiceClientBuilder().connectionString(azureBlobConnectionString);
        BlobClient blobClient = clientBuilder.buildClient().getBlobContainerClient(containerName).getBlobClient(blobName);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blobClient.downloadStream(outputStream);

        byte[] data = outputStream.toByteArray();
        LandingConfigDTO wrapper = objectMapper.readValue(data, LandingConfigDTO.class);
        return wrapper;
    }


    public void writeConfigToAzureBlob(LandingConfigDTO updatedConfig, String blobName) throws IOException {
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
    }
    
    
}

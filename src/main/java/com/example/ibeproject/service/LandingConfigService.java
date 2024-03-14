package com.example.ibeproject.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.example.ibeproject.dto.landing.SearchFormConfigDTO;
import com.example.ibeproject.dto.landing.SearchFormConfigWrapperDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        this.containerName= containerName;
    }

    public SearchFormConfigDTO loadConfigFromAzureBlob(String blobName) throws IOException {
        BlobServiceClientBuilder clientBuilder = new BlobServiceClientBuilder().connectionString(azureBlobConnectionString);
        BlobClient blobClient = clientBuilder.buildClient().getBlobContainerClient(containerName).getBlobClient(blobName);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blobClient.downloadStream(outputStream);

        byte[] data = outputStream.toByteArray();
        SearchFormConfigWrapperDTO wrapper = objectMapper.readValue(data, SearchFormConfigWrapperDTO.class);
        return wrapper.getSearchFormConfig();
    }
}


package com.example.ibeproject.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.example.ibeproject.dto.header.HeaderConfigDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    public HeaderConfigDTO loadConfigFromAzureBlob(String blobName) throws IOException {
        BlobServiceClientBuilder clientBuilder = new BlobServiceClientBuilder().connectionString(azureBlobConnectionString);
        BlobClient blobClient = clientBuilder.buildClient().getBlobContainerClient(containerName).getBlobClient(blobName);

        byte[] data = blobClient.downloadContent().toBytes();
        return objectMapper.readValue(new ByteArrayInputStream(data), HeaderConfigDTO.class);
    }
}

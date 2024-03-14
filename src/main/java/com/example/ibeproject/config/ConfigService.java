package com.example.ibeproject.config;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ConfigService {
    private final ObjectMapper objectMapper;

    public ConfigService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public SearchFormConfig loadConfigFromFile(String filePath) throws IOException {
        SearchFormConfigWrapper wrapper = objectMapper.readValue(new File(filePath), SearchFormConfigWrapper.class);
        return wrapper.getSearchFormConfig();
    }
}


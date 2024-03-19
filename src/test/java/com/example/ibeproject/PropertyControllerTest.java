package com.example.ibeproject;

import com.example.ibeproject.controller.PropertyController;
import com.example.ibeproject.dto.property.PropertyDTO;
import com.example.ibeproject.dto.property.PropertyListResponseDTO;
import com.example.ibeproject.service.PropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PropertyControllerTest {

    @Mock
    private PropertyService propertyService;

    @InjectMocks
    private PropertyController propertyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllProperties() {
        List<PropertyDTO> propertyDTOList = new ArrayList<>();
        for (int i = 1; i <= 18; i++) {
            PropertyDTO propertyDTO = new PropertyDTO();
            propertyDTO.setPropertyId(i);
            propertyDTO.setPropertyName("Team " + i + " Hotel");
            propertyDTOList.add(propertyDTO);
        }

        when(propertyService.getAllProperties()).thenReturn(propertyDTOList);

        ResponseEntity<PropertyListResponseDTO> responseEntity = propertyController.getAllProperties();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        PropertyListResponseDTO responseBody = responseEntity.getBody();
        assertEquals(18, responseBody.getProperties().size());
    }
}


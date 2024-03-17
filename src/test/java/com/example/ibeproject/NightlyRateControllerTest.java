package com.example.ibeproject;

import com.example.ibeproject.controller.NightlyRateController;
import com.example.ibeproject.dto.nightlyrate.NightlyRateResponseDTO;
import com.example.ibeproject.service.NightlyRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NightlyRateControllerTest {

    @Mock
    private NightlyRateService nightlyRateService;

    @InjectMocks
    private NightlyRateController nightlyRateController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetAllNightlyRates() {
        Map<String, Double> nightlyRates = new HashMap<>();
        nightlyRates.put("2024-03-01T00:00:00.000Z", 50.0);

        when(nightlyRateService.getMinimumNightlyRates(1, 61)).thenReturn(nightlyRates);

        ResponseEntity<NightlyRateResponseDTO> responseEntity = nightlyRateController.getAllNightlyRates(1, 61);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        NightlyRateResponseDTO responseDTO = responseEntity.getBody();
        assertEquals(nightlyRates, responseDTO.getNightlyRates());
    }
}

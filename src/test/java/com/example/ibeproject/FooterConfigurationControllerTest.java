package com.example.ibeproject;

import com.example.ibeproject.controller.FooterConfigurationController;
import com.example.ibeproject.dto.footer.FooterConfigDTO;
import com.example.ibeproject.service.FooterConfigService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FooterConfigurationControllerTest {

    @Mock
    private FooterConfigService footerConfigService;

    @InjectMocks
    private FooterConfigurationController controller;

    @Test
    public void testGetFooterConfig() {
        // Mocking the service response
        FooterConfigDTO mockConfig = new FooterConfigDTO();
        mockConfig.setLogo("src/assets/company-logo-white.png");
        mockConfig.setCompanyName("Kickdrum Technology Group LLC.");

        when(footerConfigService.loadConfigFromAzureBlob(eq(""))).thenReturn(mockConfig);

        // Calling the controller method
        ResponseEntity<FooterConfigDTO> responseEntity = controller.getFooterConfig();

        // Verifying the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(mockConfig, responseEntity.getBody());

        // Verifying service method invocation
        verify(footerConfigService, times(1)).loadConfigFromAzureBlob(anyString());
    }

    @Test
    public void testUpdateFooterConfig() {
        // Mocking the request body
        FooterConfigDTO updatedConfig = new FooterConfigDTO();
        updatedConfig.setLogo("src/assets/company-logo-updated.png");
        updatedConfig.setCompanyName("Updated Company Name");

        // Calling the controller method
        ResponseEntity<String> responseEntity = controller.updateFooterConfig(updatedConfig);

        // Verifying the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Footer config updated successfully", responseEntity.getBody());

        // Verifying service method invocation
        verify(footerConfigService, times(1)).writeConfigToAzureBlob(eq(updatedConfig), anyString());
    }
}

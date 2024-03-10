package com.example.ibeproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HealthTests {
    @LocalServerPort
    private int localHostPort;

    @Test
    void healthEndpointReturnsUpStatus() {
        String actuatorURL = "http://localhost:" + localHostPort + "/actuator/health";

        WebTestClient.bindToServer().baseUrl(actuatorURL)
                .build()
                .get()
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"status\":\"UP\"}");
    }

}
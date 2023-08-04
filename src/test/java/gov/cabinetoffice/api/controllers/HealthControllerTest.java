package gov.cabinetoffice.api.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class HealthControllerTest {

    private final HealthController controllerUnderTest = new HealthController();

    @Test
    void getHealth_returnsExpectedResponse() {

        final ResponseEntity<String> response = controllerUnderTest.getHealth();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Application is running");
    }
}
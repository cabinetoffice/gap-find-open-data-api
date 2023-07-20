package gov.cabinetoffice.api.controllers;

import gov.cabinetoffice.api.dtos.CreateApiKeyDTO;
import gov.cabinetoffice.api.services.ApiGatewayService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ApiGatewayControllerTest {

	private final String API_KEY_NAME = "apikeyName";

	private final String API_KEY_DESCRIPTION = "apikeyDescription";

	@Mock
	private ApiGatewayService apiGatewayService;

	@InjectMocks
	private ApiGatewayController controllerUnderTest;

	@Test
	void createKey() {

		final CreateApiKeyDTO createApiKeyDTO = CreateApiKeyDTO.builder()
			.keyName(API_KEY_NAME)
			.keyDescription(API_KEY_DESCRIPTION)
			.build();
		final ResponseEntity<String> response = controllerUnderTest.createKey(createApiKeyDTO);

		verify(apiGatewayService).createApiKeys(API_KEY_NAME, API_KEY_DESCRIPTION);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo("Key created successfully");
	}

	@Test
	void deleteKey() {
		final ResponseEntity<String> response = controllerUnderTest.deleteKey(API_KEY_NAME);

		verify(apiGatewayService).deleteApiKeys(API_KEY_NAME);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo("Key deleted successfully");
	}

}
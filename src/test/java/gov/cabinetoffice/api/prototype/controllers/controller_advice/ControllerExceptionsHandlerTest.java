package gov.cabinetoffice.api.prototype.controllers.controller_advice;

import gov.cabinetoffice.api.prototype.exceptions.ApiKeyAlreadyExistException;
import gov.cabinetoffice.api.prototype.exceptions.ApiKeyDoesNotExistException;
import gov.cabinetoffice.api.prototype.exceptions.ApplicationFormNotFoundException;
import gov.cabinetoffice.api.prototype.exceptions.SubmissionNotFoundException;
import gov.cabinetoffice.api.prototype.models.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import software.amazon.awssdk.services.apigateway.model.ApiGatewayException;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ControllerExceptionsHandlerTest {

	@Mock
	private ApplicationFormNotFoundException applicationFormNotFoundException;

	@Mock
	private SubmissionNotFoundException submissionNotFoundException;

	@Mock
	private MethodArgumentTypeMismatchException methodArgumentTypeMismatchException;

	@Mock
	private ApiKeyAlreadyExistException apiKeyAlreadyExistException;

	@Mock
	private ApiKeyDoesNotExistException apiKeyDoesNotExistException;

	@Mock
	private ApiGatewayException apiGatewayException;

	@Mock
	private WebRequest webRequest;

	@InjectMocks
	private ControllerExceptionsHandler controllerExceptionsHandler;

	@Test
	void testHandleException_applicationNotFound() {
		final String errorMessage = "Application form not found";

		when(applicationFormNotFoundException.getMessage()).thenReturn(errorMessage);

		final ResponseEntity<Object> responseEntity = controllerExceptionsHandler
			.handleException(applicationFormNotFoundException, webRequest);

		final String errorMessageFromResponse = ((ErrorMessage) Objects.requireNonNull(responseEntity.getBody()))
			.getMessage();
		assertThat(HttpStatus.NOT_FOUND).isEqualTo(responseEntity.getStatusCode());
		assertThat(errorMessage).isEqualTo(errorMessageFromResponse);
	}

	@Test
	void testHandleException_SubmissionNotFound() {
		final String errorMessage = "Submission not found";

		when(submissionNotFoundException.getMessage()).thenReturn(errorMessage);

		final ResponseEntity<Object> responseEntity = controllerExceptionsHandler
			.handleException(submissionNotFoundException, webRequest);

		final String errorMessageFromResponse = ((ErrorMessage) Objects.requireNonNull(responseEntity.getBody()))
			.getMessage();
		assertThat(HttpStatus.NOT_FOUND).isEqualTo(responseEntity.getStatusCode());
		assertThat(errorMessage).isEqualTo(errorMessageFromResponse);
	}

	@Test
	void testHandleException_MethodArgumentTypeMismatchException() {

		final ResponseEntity<Object> responseEntity = controllerExceptionsHandler
			.handleMethodArgumentTypeMismatch(methodArgumentTypeMismatchException, webRequest);

		final String errorMessageFromResponse = ((ErrorMessage) Objects.requireNonNull(responseEntity.getBody()))
			.getMessage();
		assertThat(HttpStatus.BAD_REQUEST).isEqualTo(responseEntity.getStatusCode());
		assertThat(errorMessageFromResponse).isEqualTo("Invalid parameter type passed");
	}

	@Test
	void testHandleException_ApiKeyAlreadyExists() {
		final String errorMessage = "Api key already exist";

		when(apiKeyAlreadyExistException.getMessage()).thenReturn(errorMessage);

		final ResponseEntity<Object> responseEntity = controllerExceptionsHandler
			.handleException(apiKeyAlreadyExistException, webRequest);

		final String errorMessageFromResponse = ((ErrorMessage) Objects.requireNonNull(responseEntity.getBody()))
			.getMessage();
		assertThat(HttpStatus.BAD_REQUEST).isEqualTo(responseEntity.getStatusCode());
		assertThat(errorMessage).isEqualTo(errorMessageFromResponse);
	}

	@Test
	void testHandleException_ApiKeyDoesNotExist() {
		final String errorMessage = "Api does not exist";

		when(apiKeyDoesNotExistException.getMessage()).thenReturn(errorMessage);

		final ResponseEntity<Object> responseEntity = controllerExceptionsHandler
			.handleException(apiKeyDoesNotExistException, webRequest);

		final String errorMessageFromResponse = ((ErrorMessage) Objects.requireNonNull(responseEntity.getBody()))
			.getMessage();
		assertThat(HttpStatus.BAD_REQUEST).isEqualTo(responseEntity.getStatusCode());
		assertThat(errorMessage).isEqualTo(errorMessageFromResponse);
	}

	@Test
	void testHandleException_ApiGatewayException() {
		final String errorMessage = "Api gateway exception";

		when(apiGatewayException.getMessage()).thenReturn(errorMessage);

		final ResponseEntity<Object> responseEntity = controllerExceptionsHandler.handleException(apiGatewayException,
				webRequest);

		final String errorMessageFromResponse = ((ErrorMessage) Objects.requireNonNull(responseEntity.getBody()))
			.getMessage();
		assertThat(HttpStatus.BAD_REQUEST).isEqualTo(responseEntity.getStatusCode());
		assertThat(errorMessage).isEqualTo(errorMessageFromResponse);
	}

}

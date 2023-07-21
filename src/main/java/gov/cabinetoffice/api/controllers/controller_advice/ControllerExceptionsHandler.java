package gov.cabinetoffice.api.controllers.controller_advice;

import gov.cabinetoffice.api.models.ErrorMessage;
import gov.cabinetoffice.api.controllers.ApiGatewayController;
import gov.cabinetoffice.api.controllers.ApplicationFormController;
import gov.cabinetoffice.api.controllers.SubmissionsController;
import gov.cabinetoffice.api.exceptions.ApiKeyAlreadyExistException;
import gov.cabinetoffice.api.exceptions.ApiKeyDoesNotExistException;
import gov.cabinetoffice.api.exceptions.ApplicationFormNotFoundException;
import gov.cabinetoffice.api.exceptions.SubmissionNotFoundException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import software.amazon.awssdk.services.apigateway.model.ApiGatewayException;

@ControllerAdvice(
		assignableTypes = { ApplicationFormController.class, SubmissionsController.class, ApiGatewayController.class })
public class ControllerExceptionsHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { ApplicationFormNotFoundException.class })
	protected ResponseEntity<Object> handleException(ApplicationFormNotFoundException ex, WebRequest request) {
		return handleExceptionInternal(ex, ErrorMessage.builder().message(ex.getMessage()).build(), new HttpHeaders(),
				HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler(value = { SubmissionNotFoundException.class })
	protected ResponseEntity<Object> handleException(SubmissionNotFoundException ex, WebRequest request) {
		return handleExceptionInternal(ex, ErrorMessage.builder().message(ex.getMessage()).build(), new HttpHeaders(),
				HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	@ApiResponses(value = { @ApiResponse(responseCode = "400", description = "Invalid parameter type passed",
			content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ErrorMessage.class))) })
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {
		return handleExceptionInternal(ex, ErrorMessage.builder().message("Invalid parameter type passed").build(),
				new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

	}

	@ExceptionHandler(value = { ApiGatewayException.class })
	protected ResponseEntity<Object> handleException(ApiGatewayException ex, WebRequest request) {
		return handleExceptionInternal(ex, ErrorMessage.builder().message(ex.getMessage()).build(), new HttpHeaders(),
				HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(value = { ApiKeyAlreadyExistException.class })
	protected ResponseEntity<Object> handleException(ApiKeyAlreadyExistException ex, WebRequest request) {
		return handleExceptionInternal(ex, ErrorMessage.builder().message(ex.getMessage()).build(), new HttpHeaders(),
				HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(value = { ApiKeyDoesNotExistException.class })
	protected ResponseEntity<Object> handleException(ApiKeyDoesNotExistException ex, WebRequest request) {
		return handleExceptionInternal(ex, ErrorMessage.builder().message(ex.getMessage()).build(), new HttpHeaders(),
				HttpStatus.BAD_REQUEST, request);
	}

}

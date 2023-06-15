package gov.cabinetoffice.api.prototype.controllers;

import gov.cabinetoffice.api.prototype.dtos.GenericErrorDTO;
import gov.cabinetoffice.api.prototype.exceptions.ApplicationFormNotFoundException;
import gov.cabinetoffice.api.prototype.exceptions.SubmissionNotFoundException;
import gov.cabinetoffice.api.prototype.models.ErrorMessage;
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

@ControllerAdvice(assignableTypes = { ApplicationFormController.class, SubmissionsController.class })
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
                    schema = @Schema(implementation = GenericErrorDTO.class))) })
    public GenericErrorDTO handleMethodArgumentTypeMismatch() {
        return new GenericErrorDTO("Incorrect parameter type passed");
    }

}

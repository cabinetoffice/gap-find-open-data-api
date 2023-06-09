package gov.cabinetoffice.api.prototype.controllers;

import gov.cabinetoffice.api.prototype.exceptions.ApplicationFormException;
import gov.cabinetoffice.api.prototype.models.ErrorMessage;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice(assignableTypes = ApplicationFormController.class)
public class ControllerExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { ApplicationFormException.class })
    protected ResponseEntity<Object> handleException(ApplicationFormException ex, WebRequest request) {
        return handleExceptionInternal(ex, ErrorMessage.builder().message(ex.getMessage()).build(), new HttpHeaders(),
                HttpStatus.NOT_FOUND, request);
    }

}

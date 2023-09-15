package gov.cabinetoffice.api.controllers.controlleradvice;

import gov.cabinetoffice.api.models.ErrorMessage;
import gov.cabinetoffice.api.exceptions.SubmissionNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ControllerExceptionsHandlerTest {

    @Mock
    private SubmissionNotFoundException submissionNotFoundException;

    @Mock
    private MethodArgumentTypeMismatchException methodArgumentTypeMismatchException;

    @Mock
    private WebRequest webRequest;

    @InjectMocks
    private ControllerExceptionsHandler controllerExceptionsHandler;

    @Test
    void testHandleException_SubmissionNotFound() {
        final String errorMessage = "Submission not found";

        when(submissionNotFoundException.getMessage()).thenReturn(errorMessage);

        final ResponseEntity<Object> responseEntity = controllerExceptionsHandler
                .handleException(submissionNotFoundException, webRequest);

        final String errorMessageFromResponse = ((ErrorMessage) Objects.requireNonNull(responseEntity.getBody()))
                .getMessage();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorMessageFromResponse).isEqualTo(errorMessage);
    }

    @Test
    void testHandleException_MethodArgumentTypeMismatchException() {

        final ResponseEntity<Object> responseEntity = controllerExceptionsHandler
                .handleMethodArgumentTypeMismatch(methodArgumentTypeMismatchException, webRequest);

        final String errorMessageFromResponse = ((ErrorMessage) Objects.requireNonNull(responseEntity.getBody()))
                .getMessage();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorMessageFromResponse).isEqualTo("Invalid parameter type passed");

    }

}

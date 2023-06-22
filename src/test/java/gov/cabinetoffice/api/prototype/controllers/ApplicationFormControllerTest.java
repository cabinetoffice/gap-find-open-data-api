package gov.cabinetoffice.api.prototype.controllers;

import gov.cabinetoffice.api.prototype.config.S3ConfigProperties;
import gov.cabinetoffice.api.prototype.entities.ApplicationFormEntity;
import gov.cabinetoffice.api.prototype.entities.SchemeEntity;
import gov.cabinetoffice.api.prototype.exceptions.ApplicationFormNotFoundException;
import gov.cabinetoffice.api.prototype.models.application.ApplicationDefinition;
import gov.cabinetoffice.api.prototype.services.ApplicationFormService;
import gov.cabinetoffice.api.prototype.services.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationFormControllerTest {

	private static final int APPLICATION_ID = 1;

	final Instant instant = Instant.now();

	final SchemeEntity scheme = SchemeEntity.builder()
		.id(1)
		.version(1)
		.funderId(1)
		.lastUpdated(instant)
		.email("test@and.digital")
		.name("Test Scheme")
		.ggisIdentifier("Test GGIS Identifier")
		.build();

	final ApplicationFormEntity application = ApplicationFormEntity.builder()
		.grantApplicationId(APPLICATION_ID)
		.applicationName("Test Application")
		.created(instant)
		.lastUpdated(instant)
		.definition(new ApplicationDefinition())
		.grantSchemeId(scheme.getId())
		.version(1)
		.lastUpdateBy(1)
		.build();

	@Mock
	private ApplicationFormService applicationFormService;

	private ApplicationFormController controllerUnderTest;

	@BeforeEach
	void setup() {
		controllerUnderTest = new ApplicationFormController(applicationFormService);
	}

	@Test
	void getApplicationById_returnsExpectedResponse() {
		when(applicationFormService.getApplicationById(APPLICATION_ID)).thenReturn(application);

		final ResponseEntity<ApplicationFormEntity> response = controllerUnderTest.getApplicationById(APPLICATION_ID);

		verify(applicationFormService).getApplicationById(APPLICATION_ID);
		assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
		assertThat(response.getBody()).isEqualTo(application);

	}

	@Test
	void getApplicationById_returns404WhenNoApplicationFound() {
		when(applicationFormService.getApplicationById(APPLICATION_ID))
				.thenThrow(new ApplicationFormNotFoundException("error"));
		final Exception result = assertThrows(ApplicationFormNotFoundException.class,
				() -> controllerUnderTest.getApplicationById(APPLICATION_ID));
		verify(applicationFormService).getApplicationById(APPLICATION_ID);
		assertThat(result.getMessage()).contains("error");
	}

}

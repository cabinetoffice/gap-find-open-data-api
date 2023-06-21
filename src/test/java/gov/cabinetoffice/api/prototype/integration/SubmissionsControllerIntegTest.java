package gov.cabinetoffice.api.prototype.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.cabinetoffice.api.prototype.controllers.SubmissionsController;
import gov.cabinetoffice.api.prototype.controllers.controller_advice.ControllerExceptionsHandler;
import gov.cabinetoffice.api.prototype.dtos.submission.SubmissionDTO;
import gov.cabinetoffice.api.prototype.dtos.submission.SubmissionsDTO;
import gov.cabinetoffice.api.prototype.entities.*;
import gov.cabinetoffice.api.prototype.exceptions.SubmissionNotFoundException;
import gov.cabinetoffice.api.prototype.mappers.SubmissionMapper;
import gov.cabinetoffice.api.prototype.mappers.SubmissionMapperImpl;
import gov.cabinetoffice.api.prototype.services.SubmissionsService;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.ZonedDateTime;
import java.util.List;

import static gov.cabinetoffice.api.prototype.test_data_generator.RandomSubmissionGenerator.randomSubmission;
import static gov.cabinetoffice.api.prototype.test_data_generator.RandomSubmissionGenerator.randomSubmissionDefinition;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubmissionsController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = { SubmissionsController.class, ControllerExceptionsHandler.class })
class SubmissionsControllerIntegTest {

	private static final String BASE_PATH = "/submissions/";

	private final Integer APPLICATION_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SubmissionsService submissionsService;

	@Autowired
	private ObjectMapper objectMapper;

	@Spy
	private SubmissionMapper submissionMapper = new SubmissionMapperImpl();

	@Test
	void getSubmissionByApplicationId_found() throws Exception {
		final ZonedDateTime zonedDateTime = ZonedDateTime.now();
		final ApplicationFormEntity applicationForm = ApplicationFormEntity.builder()
			.grantApplicationId(APPLICATION_ID)
			.applicationName("test")
			.grantApplicationId(APPLICATION_ID)
			.build();

		final Submission submission = randomSubmission()
			.definition(randomSubmissionDefinition(randomSubmissionDefinition().build()).build())
			.gapId("testGapID")
			.applicant(GrantApplicant.builder()
				.organisationProfile(GrantApplicantOrganisationProfile.builder().legalName("testLegalName").build())
				.build())
			.scheme(SchemeEntity.builder().id(1).name("testSchemeName").build())
			.submittedDate(zonedDateTime)
			.application(applicationForm)
			.build();

		when(submissionMapper.submissionToSubmissionDto(submission)).thenCallRealMethod();

		final SubmissionDTO submissionDTO = submissionMapper.submissionToSubmissionDto(submission);
		final SubmissionsDTO response = SubmissionsDTO.builder().submissions(List.of(submissionDTO)).build();

		when(submissionsService.getSubmissionByApplicationId(APPLICATION_ID)).thenReturn(response);

		final String expectedJson = objectMapper.writeValueAsString(response);

		mockMvc.perform(get(BASE_PATH + APPLICATION_ID).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(expectedJson));
	}

	@Test
	void getSubmissionByApplicationId_notFound() throws Exception {
		final String errorMsg = "No submissions found with application id " + APPLICATION_ID;

		when(submissionsService.getSubmissionByApplicationId(APPLICATION_ID))
			.thenThrow(new SubmissionNotFoundException(errorMsg));

		mockMvc.perform(get(BASE_PATH + APPLICATION_ID).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value(errorMsg));
	}

	@Test
	void getSubmissionByApplicationId_invalidArgument() throws Exception {
		final ResultActions actions = mockMvc.perform(get(BASE_PATH + "invalidParameterType"));

		final String errorMessage = actions.andReturn().getResponse().getContentAsString();
		final int errorCode = actions.andReturn().getResponse().getStatus();
		assertThat(errorCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(errorMessage).contains("Invalid parameter type passed");

	}

}
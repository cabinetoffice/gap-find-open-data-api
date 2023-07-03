package gov.cabinetoffice.api.prototype.services;

import gov.cabinetoffice.api.prototype.dtos.submission.SubmissionDTO;
import gov.cabinetoffice.api.prototype.dtos.submission.SubmissionListDTO;
import gov.cabinetoffice.api.prototype.entities.ApplicationFormEntity;
import gov.cabinetoffice.api.prototype.entities.GrantApplicant;
import gov.cabinetoffice.api.prototype.entities.GrantApplicantOrganisationProfile;
import gov.cabinetoffice.api.prototype.entities.SchemeEntity;
import gov.cabinetoffice.api.prototype.entities.Submission;
import gov.cabinetoffice.api.prototype.exceptions.SubmissionNotFoundException;
import gov.cabinetoffice.api.prototype.mappers.SubmissionMapper;
import gov.cabinetoffice.api.prototype.mappers.SubmissionMapperImpl;
import gov.cabinetoffice.api.prototype.repositories.SubmissionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;

import static gov.cabinetoffice.api.prototype.test_data_generator.RandomSubmissionGenerator.randomSubmission;
import static gov.cabinetoffice.api.prototype.test_data_generator.RandomSubmissionGenerator.randomSubmissionDefinition;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubmissionsServiceTest {

	@Mock
	private SubmissionRepository submissionRepository;

	@InjectMocks
	private SubmissionsService submissionsService;

	@Spy
	private final SubmissionMapper submissionMapper = new SubmissionMapperImpl();

	private final int APPLICATION_ID = 1;

	@Test
	void getSubmissionByApplicationId_found() {
		final ZonedDateTime zonedDateTime = ZonedDateTime.now();

		final ApplicationFormEntity applicationForm = ApplicationFormEntity.builder()
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

		when(submissionRepository.findByApplicationGrantApplicationId(APPLICATION_ID)).thenReturn(List.of(submission));
		when(submissionMapper.submissionToSubmissionDto(submission)).thenCallRealMethod();

		final SubmissionListDTO response = submissionsService.getSubmissionByApplicationId(APPLICATION_ID);

		final SubmissionDTO submissionDTO = response.getSubmissions().get(0);
		final SubmissionListDTO expectedResult = SubmissionListDTO.builder()
			.submissions(List.of(submissionDTO))
			.build();

		assertEquals(submissionMapper.submissionToSubmissionDto(submission), submissionDTO);

		verify(submissionRepository).findByApplicationGrantApplicationId(APPLICATION_ID);

		assertThat(response).isEqualTo(expectedResult);
	}

	@Test
	void getSubmissionByApplicationId_notFound() {
		when(submissionRepository.findByApplicationGrantApplicationId(APPLICATION_ID)).thenReturn(List.of());
		final Throwable exception = assertThrows(SubmissionNotFoundException.class,
				() -> submissionsService.getSubmissionByApplicationId(APPLICATION_ID));

		verify(submissionRepository).findByApplicationGrantApplicationId(APPLICATION_ID);
		assertThat(exception.getMessage()).isEqualTo("No submissions found with application id " + APPLICATION_ID);
	}

}
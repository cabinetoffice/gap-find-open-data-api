package gov.cabinetoffice.api.services;

import gov.cabinetoffice.api.dtos.submission.SubmissionDTO;
import gov.cabinetoffice.api.dtos.submission.SubmissionListDTO;
import gov.cabinetoffice.api.entities.*;
import gov.cabinetoffice.api.exceptions.SubmissionNotFoundException;
import gov.cabinetoffice.api.mappers.SubmissionMapper;
import gov.cabinetoffice.api.mappers.SubmissionMapperImpl;
import gov.cabinetoffice.api.repositories.SubmissionRepository;
import gov.cabinetoffice.api.test_data_generator.RandomSubmissionGenerator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;

import static gov.cabinetoffice.api.test_data_generator.RandomSubmissionGenerator.randomSubmission;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
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
			.definition(RandomSubmissionGenerator.randomSubmissionDefinition(RandomSubmissionGenerator.randomSubmissionDefinition().build()).build())
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

		assertThat(submissionDTO).isEqualTo(submissionMapper.submissionToSubmissionDto(submission));

		verify(submissionRepository).findByApplicationGrantApplicationId(APPLICATION_ID);

		assertThat(response).isEqualTo(expectedResult);
	}

	@Test
	void getSubmissionByApplicationId_notFound() {
		when(submissionRepository.findByApplicationGrantApplicationId(APPLICATION_ID)).thenReturn(List.of());

		assertThatExceptionOfType(SubmissionNotFoundException.class)
				.isThrownBy(() -> submissionsService.getSubmissionByApplicationId(APPLICATION_ID))
				.withMessage("No submissions found with application id " + APPLICATION_ID);

		verify(submissionRepository).findByApplicationGrantApplicationId(APPLICATION_ID);

	}

}
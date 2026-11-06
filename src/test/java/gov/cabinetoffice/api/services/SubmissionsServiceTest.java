package gov.cabinetoffice.api.services;

import gov.cabinetoffice.api.dtos.submission.ApplicationDTO;
import gov.cabinetoffice.api.dtos.submission.ApplicationListDTO;
import gov.cabinetoffice.api.dtos.submission.SubmissionDTO;
import gov.cabinetoffice.api.dtos.submission.SubmissionDetailDTO;
import gov.cabinetoffice.api.entities.ApplicationFormEntity;
import gov.cabinetoffice.api.entities.SchemeEntity;
import gov.cabinetoffice.api.entities.Submission;
import gov.cabinetoffice.api.enums.SubmissionStatus;
import gov.cabinetoffice.api.exceptions.SubmissionNotFoundException;
import gov.cabinetoffice.api.mappers.SubmissionMapper;
import gov.cabinetoffice.api.repositories.SubmissionJDBCRepository;
import gov.cabinetoffice.api.repositories.SubmissionRepository;

import static gov.cabinetoffice.api.test_data_generator.RandomSubmissionGenerator.randomSubmission;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class SubmissionsServiceTest {

	@Mock
	private SubmissionRepository submissionRepository;
	@Mock
	private SubmissionJDBCRepository submissionJDBCRepository;
	@Mock
	private SubmissionMapper submissionMapper;

	@InjectMocks
	private SubmissionsService submissionsService;

	private final int FUNDING_ORG_ID = 1;
	private final int APPLICATION_ID = 1234;
	private final String GGIS_REFERENCE_NUMBER = "SCH-000003589";

	@Test
	void getSubmissionsByFundingOrgIdAndGgisReferenceNum_ReturnsExpectedData() {

		final Submission submission = randomSubmission()
				.build();
		final List<Submission> submissions = List.of(submission);

		final ApplicationDTO application = ApplicationDTO.builder()
				.applicationId(APPLICATION_ID)
				.ggisReferenceNumber("SCH-001")
				.applicationFormName("Woodland services grant")
				.grantAdminEmailAddress("gavin.cook@and.digital")
				.build();
		final List<ApplicationDTO> applications = List.of(application);

		final ApplicationListDTO applicationListDTO = ApplicationListDTO.builder()
				.applications(applications)
				.build();
		
		when(submissionJDBCRepository.getApplicationSubmissionsByFundingOrganisationIdAndGgisIdentifier(FUNDING_ORG_ID, GGIS_REFERENCE_NUMBER))
				.thenReturn(applicationListDTO);

		when(submissionRepository.countByStatusAndApplicationGrantApplicationId(SubmissionStatus.SUBMITTED, APPLICATION_ID))
				.thenReturn(submissions.size());
		when(submissionRepository.findByStatusAndApplicationGrantApplicationId(eq(SubmissionStatus.SUBMITTED), eq(APPLICATION_ID), any(Pageable.class)))
				.thenReturn(submissions);

		assertThat(application.getSubmissions()).isEmpty();

		final ApplicationListDTO methodResponse = submissionsService.getSubmissionsByFundingOrgIdAndGgisReferenceNum(FUNDING_ORG_ID, GGIS_REFERENCE_NUMBER);

		verify(submissionJDBCRepository).getApplicationSubmissionsByFundingOrganisationIdAndGgisIdentifier(FUNDING_ORG_ID, GGIS_REFERENCE_NUMBER);
		verify(submissionRepository, times(1)).countByStatusAndApplicationGrantApplicationId(SubmissionStatus.SUBMITTED, APPLICATION_ID);
		verify(submissionRepository, times(1)).findByStatusAndApplicationGrantApplicationId(eq(SubmissionStatus.SUBMITTED), eq(APPLICATION_ID), any(Pageable.class));
		assertThat(methodResponse.getApplications()).isEqualTo(applications);
	}

	@Test
	void getSubmissionsByFundingOrgIdAndGgisReferenceNum_ThrowsSubmissionNotFoundException() {
		final String msg = "No submissions found";
		when(submissionJDBCRepository.getApplicationSubmissionsByFundingOrganisationIdAndGgisIdentifier(FUNDING_ORG_ID, GGIS_REFERENCE_NUMBER))
				.thenThrow(new SubmissionNotFoundException(msg));

		assertThatExceptionOfType(SubmissionNotFoundException.class)
				.isThrownBy(() -> submissionsService.getSubmissionsByFundingOrgIdAndGgisReferenceNum(FUNDING_ORG_ID, GGIS_REFERENCE_NUMBER))
				.withMessage(msg);
	}

	@Test
	void getSubmissionsByFundingOrgId_ReturnsExpectedData() {

		final Submission submission = randomSubmission()
				.build();
		final List<Submission> submissions = List.of(submission);

		final ApplicationDTO application = ApplicationDTO.builder()
				.applicationId(APPLICATION_ID)
				.ggisReferenceNumber("SCH-001")
				.applicationFormName("Woodland services grant")
				.grantAdminEmailAddress("gavin.cook@and.digital")
				.build();
		final List<ApplicationDTO> applications = List.of(application);

		final ApplicationListDTO applicationListDTO = ApplicationListDTO.builder()
				.applications(applications)
				.build();
		
		when(submissionJDBCRepository.getApplicationSubmissionsByFundingOrganisationId(FUNDING_ORG_ID))
				.thenReturn(applicationListDTO);

		when(submissionRepository.countByStatusAndApplicationGrantApplicationId(SubmissionStatus.SUBMITTED, APPLICATION_ID))
				.thenReturn(submissions.size());
		when(submissionRepository.findByStatusAndApplicationGrantApplicationId(eq(SubmissionStatus.SUBMITTED), eq(APPLICATION_ID), any(Pageable.class)))
				.thenReturn(submissions);

		assertThat(application.getSubmissions()).isEmpty();

		final ApplicationListDTO methodResponse = submissionsService.getSubmissionsByFundingOrgId(FUNDING_ORG_ID);

		verify(submissionJDBCRepository).getApplicationSubmissionsByFundingOrganisationId(FUNDING_ORG_ID);
		verify(submissionRepository, times(1)).countByStatusAndApplicationGrantApplicationId(SubmissionStatus.SUBMITTED, APPLICATION_ID);
		verify(submissionRepository, times(1)).findByStatusAndApplicationGrantApplicationId(eq(SubmissionStatus.SUBMITTED), eq(APPLICATION_ID), any(Pageable.class));
		assertThat(methodResponse.getApplications()).isEqualTo(applications);
	}

	@Test
	void getSubmissionsByFundingOrgId_ThrowsSubmissionNotFoundException() {
		final String msg = "No submissions found";
		when(submissionJDBCRepository.getApplicationSubmissionsByFundingOrganisationId(FUNDING_ORG_ID))
				.thenThrow(new SubmissionNotFoundException(msg));

		assertThatExceptionOfType(SubmissionNotFoundException.class)
				.isThrownBy(() -> submissionsService.getSubmissionsByFundingOrgId(FUNDING_ORG_ID))
				.withMessage(msg);
	}

	@Test
	void getSubmissionByIdAndFundingOrgId_ReturnsExpectedData() {
		final UUID submissionId = UUID.randomUUID();
		final Submission submission = randomSubmission()
				.id(submissionId)
				.scheme(SchemeEntity.builder().funderId(FUNDING_ORG_ID).ggisIdentifier(GGIS_REFERENCE_NUMBER).build())
				.application(ApplicationFormEntity.builder().applicationName("Test Application").build())
				.build();
		final SubmissionDTO submissionDTO = SubmissionDTO.builder()
				.submissionId(submissionId)
				.gapId("GAP-ID")
				.grantApplicantEmailAddress("applicant@example.com")
				.submittedTimeStamp(ZonedDateTime.now())
				.sections(List.of())
				.build();

		when(submissionRepository.findByIdAndStatusAndSchemeFunderId(submissionId, SubmissionStatus.SUBMITTED, FUNDING_ORG_ID))
				.thenReturn(Optional.of(submission));
		when(submissionMapper.submissionToSubmissionDto(submission)).thenReturn(submissionDTO);

		final SubmissionDetailDTO response = submissionsService.getSubmissionByIdAndFundingOrgId(submissionId, FUNDING_ORG_ID);

		assertThat(response.getSubmissionId()).isEqualTo(submissionId);
		assertThat(response.getGapId()).isEqualTo("GAP-ID");
		assertThat(response.getGgisReferenceNumber()).isEqualTo(GGIS_REFERENCE_NUMBER);
		assertThat(response.getApplicationFormName()).isEqualTo("Test Application");
		assertThat(response.getGrantApplicantEmailAddress()).isEqualTo("applicant@example.com");
		assertThat(response.getSubmittedTimeStamp()).isEqualTo(submissionDTO.getSubmittedTimeStamp());
		assertThat(response.getSections()).isEqualTo(submissionDTO.getSections());
	}

	@Test
	void getSubmissionByIdAndFundingOrgId_ThrowsSubmissionNotFoundException_WhenNotOwnedOrNotSubmitted() {
		final UUID submissionId = UUID.randomUUID();

		when(submissionRepository.findByIdAndStatusAndSchemeFunderId(submissionId, SubmissionStatus.SUBMITTED, FUNDING_ORG_ID))
				.thenReturn(Optional.empty());

		assertThatExceptionOfType(SubmissionNotFoundException.class)
				.isThrownBy(() -> submissionsService.getSubmissionByIdAndFundingOrgId(submissionId, FUNDING_ORG_ID))
				.withMessage("No submission found with id " + submissionId);
		verifyNoInteractions(submissionMapper);
	}
}

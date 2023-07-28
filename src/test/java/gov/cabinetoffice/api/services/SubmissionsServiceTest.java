package gov.cabinetoffice.api.services;

import gov.cabinetoffice.api.dtos.submission.SubmissionDTO;
import gov.cabinetoffice.api.dtos.submission.SubmissionListDTO;
import gov.cabinetoffice.api.entities.Submission;
import gov.cabinetoffice.api.exceptions.SubmissionNotFoundException;
import gov.cabinetoffice.api.mappers.SubmissionMapper;
import gov.cabinetoffice.api.repositories.SubmissionRepository;
import static gov.cabinetoffice.api.test_data_generator.RandomSubmissionGenerator.randomSubmission;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class SubmissionsServiceTest {

	@Mock
	private SubmissionRepository submissionRepository;
	@Mock
	private SubmissionMapper submissionMapper;
	@InjectMocks
	private SubmissionsService submissionsService;
	private final int FUNDING_ORG_ID = 1;
	private final String GGIS_REFERENCE_NUMBER = "SCH-000003589";

	@Test
	void getSubmissionsByFundingOrgIdAndGgisReferenceNum_ReturnsExpectedData() {

		final Submission submission = randomSubmission()
				.build();
		final List<Submission> submissions = List.of(submission);

		final SubmissionDTO submissionDto = SubmissionDTO
				.builder()
				.build();
		final List<SubmissionDTO> submissionDTOList = List.of(submissionDto);

		when(submissionRepository.findBySchemeFunderIdAndSchemeGgisIdentifier(FUNDING_ORG_ID, GGIS_REFERENCE_NUMBER))
				.thenReturn(submissions);

		when(submissionMapper.submissionToSubmissionDto(submission))
				.thenReturn(submissionDto);

		final SubmissionListDTO methodResponse = submissionsService.getSubmissionsByFundingOrgIdAndGgisReferenceNum(FUNDING_ORG_ID, GGIS_REFERENCE_NUMBER);

		verify(submissionRepository).findBySchemeFunderIdAndSchemeGgisIdentifier(FUNDING_ORG_ID, GGIS_REFERENCE_NUMBER);
		assertThat(methodResponse.getNumberOfResults()).isEqualTo(List.of(submission).size());
		assertThat(methodResponse.getSubmissions()).isEqualTo(submissionDTOList);
	}

	@Test
	void getSubmissionsByFundingOrgIdAndGgisReferenceNum_ThrowsSubmissionNotFoundException() {

		when(submissionRepository.findBySchemeFunderIdAndSchemeGgisIdentifier(FUNDING_ORG_ID, GGIS_REFERENCE_NUMBER))
				.thenReturn(Collections.emptyList());

		assertThatExceptionOfType(SubmissionNotFoundException.class)
				.isThrownBy(() -> submissionsService.getSubmissionsByFundingOrgIdAndGgisReferenceNum(FUNDING_ORG_ID, GGIS_REFERENCE_NUMBER))
				.withMessage("No submissions found");
	}

	@Test
	void getSubmissionsByFundingOrgId_ReturnsExpectedData() {

		final Submission submission = randomSubmission()
				.build();
		final List<Submission> submissions = List.of(submission);

		final SubmissionDTO submissionDto = SubmissionDTO
				.builder()
				.build();
		final List<SubmissionDTO> submissionDTOList = List.of(submissionDto);

		when(submissionRepository.findBySchemeFunderId(FUNDING_ORG_ID))
				.thenReturn(submissions);

		when(submissionMapper.submissionToSubmissionDto(submission))
				.thenReturn(submissionDto);

		final SubmissionListDTO methodResponse = submissionsService.getSubmissionsByFundingOrgId(FUNDING_ORG_ID);

		verify(submissionRepository).findBySchemeFunderId(FUNDING_ORG_ID);
		assertThat(methodResponse.getNumberOfResults()).isEqualTo(List.of(submission).size());
		assertThat(methodResponse.getSubmissions()).isEqualTo(submissionDTOList);
	}

	@Test
	void getSubmissionsByFundingOrgId_ThrowsSubmissionNotFoundException() {

		when(submissionRepository.findBySchemeFunderId(FUNDING_ORG_ID))
				.thenReturn(Collections.emptyList());

		assertThatExceptionOfType(SubmissionNotFoundException.class)
				.isThrownBy(() -> submissionsService.getSubmissionsByFundingOrgId(FUNDING_ORG_ID))
				.withMessage("No submissions found");
	}
}
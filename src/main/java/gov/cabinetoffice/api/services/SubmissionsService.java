package gov.cabinetoffice.api.services;

import gov.cabinetoffice.api.dtos.submission.ApplicationListDTO;
import gov.cabinetoffice.api.entities.Submission;
import gov.cabinetoffice.api.enums.SubmissionStatus;
import gov.cabinetoffice.api.mappers.SubmissionMapper;
import gov.cabinetoffice.api.repositories.SubmissionJDBCRepository;
import gov.cabinetoffice.api.repositories.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionsService {

	private final SubmissionRepository submissionRepository;
	private final SubmissionJDBCRepository submissionJdbcRepository;
	private final SubmissionMapper submissionMapper;

	public ApplicationListDTO getSubmissionsByFundingOrgId(int fundingOrgId) {
		final ApplicationListDTO applicationDTO = submissionJdbcRepository.getApplicationSubmissionsByFundingOrganisationId(fundingOrgId);

		applicationDTO.getApplications().forEach(a -> {
			final List<Submission> submissions = submissionRepository.findByStatusAndApplicationGrantApplicationId(SubmissionStatus.SUBMITTED, a.getApplicationId());
			a.setSubmissions(
					submissions.stream()
							.map(submissionMapper::submissionToSubmissionDto)
							.toList()
			);
		});

		return applicationDTO;
	}

	public ApplicationListDTO getSubmissionsByFundingOrgIdAndGgisReferenceNum(int fundingOrgId, String ggisReferenceNumber) {
		final ApplicationListDTO applicationDTO = submissionJdbcRepository.getApplicationSubmissionsByFundingOrganisationIdAndGgisIdentifier(fundingOrgId, ggisReferenceNumber);

		applicationDTO.getApplications().forEach(a -> {
			final List<Submission> submissions = submissionRepository.findByStatusAndApplicationGrantApplicationId(SubmissionStatus.SUBMITTED, a.getApplicationId());
			a.setSubmissions(
					submissions.stream()
							.map(submissionMapper::submissionToSubmissionDto)
							.toList()
			);
		});

		return applicationDTO;
	}
}

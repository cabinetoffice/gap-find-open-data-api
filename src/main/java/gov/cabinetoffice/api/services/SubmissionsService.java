package gov.cabinetoffice.api.services;

import gov.cabinetoffice.api.dtos.submission.ApplicationListDTO;
import gov.cabinetoffice.api.dtos.submission.CountResponseDTO;
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

	private static final int PAGE_SIZE = 100;

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

	public ApplicationListDTO getSubmissionsByFundingOrgId(int fundingOrgId, int page) {
		final ApplicationListDTO applicationDTO = submissionJdbcRepository.getApplicationSubmissionsByFundingOrganisationId(fundingOrgId, page);

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
		// default to first page when no page provided
		return getSubmissionsByFundingOrgIdAndGgisReferenceNum(fundingOrgId, ggisReferenceNumber, 1);
	}

	public ApplicationListDTO getSubmissionsByFundingOrgIdAndGgisReferenceNum(int fundingOrgId, String ggisReferenceNumber, int page) {
		final ApplicationListDTO applicationDTO = submissionJdbcRepository.getApplicationSubmissionsByFundingOrganisationIdAndGgisIdentifier(fundingOrgId, ggisReferenceNumber, page);

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

	public CountResponseDTO getSubmissionsCountByFundingOrgId(int fundingOrgId) {
		int total = submissionJdbcRepository.countSubmissionsByFundingOrganisationId(fundingOrgId);
		int totalPages = (int) Math.ceil((double) total / PAGE_SIZE);
		return CountResponseDTO.builder().totalCount(total).totalPages(totalPages).build();
	}

	public CountResponseDTO getSubmissionsCountByFundingOrgIdAndGgisReferenceNum(int fundingOrgId, String ggisReferenceNumber) {
		int total = submissionJdbcRepository.countSubmissionsByFundingOrganisationIdAndGgisIdentifier(fundingOrgId, ggisReferenceNumber);
		int totalPages = (int) Math.ceil((double) total / PAGE_SIZE);
		return CountResponseDTO.builder().totalCount(total).totalPages(totalPages).build();
	}
}

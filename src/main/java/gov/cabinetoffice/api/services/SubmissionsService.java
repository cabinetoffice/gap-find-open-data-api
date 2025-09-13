package gov.cabinetoffice.api.services;

import gov.cabinetoffice.api.dtos.submission.ApplicationListDTO;
import gov.cabinetoffice.api.dtos.submission.ApplicationSummaryDTO;
import gov.cabinetoffice.api.dtos.submission.ApplicationSummaryListDTO;
import gov.cabinetoffice.api.dtos.submission.CountResponseDTO;
import gov.cabinetoffice.api.entities.Submission;
import gov.cabinetoffice.api.enums.SubmissionStatus;
import gov.cabinetoffice.api.mappers.SubmissionMapper;
import gov.cabinetoffice.api.repositories.SubmissionJDBCRepository;
import gov.cabinetoffice.api.repositories.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissionsService {

	private static final int PAGE_SIZE = 100;

	private final SubmissionRepository submissionRepository;
	private final SubmissionJDBCRepository submissionJdbcRepository;
	private final SubmissionMapper submissionMapper;

	public ApplicationListDTO getSubmissionsByFundingOrgId(int fundingOrgId) {
		// default to first page of submissions when no page provided
		return getSubmissionsByFundingOrgId(fundingOrgId, 1);
	}

	public ApplicationListDTO getSubmissionsByFundingOrgId(int fundingOrgId, int page) {
		// fetch all applications (no application-level pagination)
		final ApplicationListDTO applicationDTO = submissionJdbcRepository.getApplicationSubmissionsByFundingOrganisationId(fundingOrgId);

		final int safePage = page < 1 ? 1 : page;
		final Pageable pageable = PageRequest.of(safePage - 1, PAGE_SIZE);

		applicationDTO.getApplications().forEach(a -> {
			final int total = submissionRepository.countByStatusAndApplicationGrantApplicationId(SubmissionStatus.SUBMITTED, a.getApplicationId());
			a.setTotalSubmissions(total);
			final int pages = (int) Math.ceil((double) total / PAGE_SIZE);
			a.setTotalSubmissionPages(pages);
			final List<Submission> submissions = submissionRepository.findByStatusAndApplicationGrantApplicationId(SubmissionStatus.SUBMITTED, a.getApplicationId(), pageable);
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
		// fetch all applications matching GGIS (no application-level pagination)
		final ApplicationListDTO applicationDTO = submissionJdbcRepository.getApplicationSubmissionsByFundingOrganisationIdAndGgisIdentifier(fundingOrgId, ggisReferenceNumber);

		final int safePage = page < 1 ? 1 : page;
		final Pageable pageable = PageRequest.of(safePage - 1, PAGE_SIZE);

		applicationDTO.getApplications().forEach(a -> {
			final int total = submissionRepository.countByStatusAndApplicationGrantApplicationId(SubmissionStatus.SUBMITTED, a.getApplicationId());
			a.setTotalSubmissions(total);
			final int pages = (int) Math.ceil((double) total / PAGE_SIZE);
			a.setTotalSubmissionPages(pages);
			final List<Submission> submissions = submissionRepository.findByStatusAndApplicationGrantApplicationId(SubmissionStatus.SUBMITTED, a.getApplicationId(), pageable);
			a.setSubmissions(
					submissions.stream()
						.map(submissionMapper::submissionToSubmissionDto)
						.toList()
			);
		});

 	return applicationDTO;
 }

 public ApplicationSummaryListDTO getApplicationSummariesByFundingOrgIdAndGgisReferenceNum(int fundingOrgId, String ggisReferenceNumber) {
 	return getApplicationSummariesByFundingOrgIdAndGgisReferenceNum(fundingOrgId, ggisReferenceNumber, 1);
 }

 public ApplicationSummaryListDTO getApplicationSummariesByFundingOrgIdAndGgisReferenceNum(int fundingOrgId, String ggisReferenceNumber, int page) {
 	final ApplicationListDTO applications = submissionJdbcRepository.getApplicationSubmissionsByFundingOrganisationIdAndGgisIdentifier(fundingOrgId, ggisReferenceNumber);

 	final List<ApplicationSummaryDTO> summaries = applications.getApplications().stream().map(a -> {
 		final int total = submissionRepository.countByStatusAndApplicationGrantApplicationId(SubmissionStatus.SUBMITTED, a.getApplicationId());
 		final int pages = (int) Math.ceil((double) total / PAGE_SIZE);
 		return ApplicationSummaryDTO.builder()
 				.applicationFormName(a.getApplicationFormName())
 				.grantAdminEmailAddress(a.getGrantAdminEmailAddress())
 				.ggisReferenceNumber(a.getGgisReferenceNumber())
 				.applicationFormVersion(a.getApplicationFormVersion())
 				.totalSubmissions(total)
 				.totalSubmissionPages(pages)
 				.build();
 	}).toList();

 	int totalApplications = submissionJdbcRepository.countApplicationsByFundingOrganisationIdAndGgisIdentifier(fundingOrgId, ggisReferenceNumber);
 	int totalApplicationPages = (int) Math.ceil((double) totalApplications / PAGE_SIZE);

 	return ApplicationSummaryListDTO.builder()
 			.numberOfResults(summaries.size())
 			.totalApplicationPages(totalApplicationPages)
 			.applications(summaries)
 			.build();
 }

 public ApplicationSummaryListDTO getApplicationSummariesByFundingOrgId(int fundingOrgId) {
 	return getApplicationSummariesByFundingOrgId(fundingOrgId, 1);
 }

	public ApplicationSummaryListDTO getApplicationSummariesByFundingOrgId(int fundingOrgId, int page) {
		final ApplicationListDTO applications = submissionJdbcRepository.getApplicationSubmissionsByFundingOrganisationId(fundingOrgId);

		final List<ApplicationSummaryDTO> summaries = applications.getApplications().stream().map(a -> {
			final int total = submissionRepository.countByStatusAndApplicationGrantApplicationId(SubmissionStatus.SUBMITTED, a.getApplicationId());
			final int pages = (int) Math.ceil((double) total / PAGE_SIZE);
			return ApplicationSummaryDTO.builder()
					.applicationFormName(a.getApplicationFormName())
					.grantAdminEmailAddress(a.getGrantAdminEmailAddress())
					.ggisReferenceNumber(a.getGgisReferenceNumber())
					.applicationFormVersion(a.getApplicationFormVersion())
					.totalSubmissions(total)
					.totalSubmissionPages(pages)
					.build();
		}).toList();

		int totalApplications = submissionJdbcRepository.countApplicationsByFundingOrganisationId(fundingOrgId);
		int totalApplicationPages = (int) Math.ceil((double) totalApplications / PAGE_SIZE);

		return ApplicationSummaryListDTO.builder()
				.numberOfResults(summaries.size())
				.totalApplicationPages(totalApplicationPages)
				.applications(summaries)
				.build();
	}

	public CountResponseDTO getSubmissionsCountByFundingOrgId(int fundingOrgId) {
		int total = submissionJdbcRepository.countApplicationsByFundingOrganisationId(fundingOrgId);
		int totalPages = (int) Math.ceil((double) total / PAGE_SIZE);
		return CountResponseDTO.builder().totalCount(total).totalPages(totalPages).build();
	}

	public CountResponseDTO getSubmissionsCountByFundingOrgIdAndGgisReferenceNum(int fundingOrgId, String ggisReferenceNumber) {
		int total = submissionJdbcRepository.countApplicationsByFundingOrganisationIdAndGgisIdentifier(fundingOrgId, ggisReferenceNumber);
		int totalPages = (int) Math.ceil((double) total / PAGE_SIZE);
		return CountResponseDTO.builder().totalCount(total).totalPages(totalPages).build();
	}
}

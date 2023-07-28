package gov.cabinetoffice.api.services;

import gov.cabinetoffice.api.dtos.submission.SubmissionListDTO;
import gov.cabinetoffice.api.entities.Submission;
import gov.cabinetoffice.api.mappers.SubmissionMapper;
import gov.cabinetoffice.api.exceptions.SubmissionNotFoundException;
import gov.cabinetoffice.api.repositories.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissionsService {

	private final SubmissionRepository submissionRepository;
	private final SubmissionMapper submissionMapper;

	public SubmissionListDTO getSubmissionsByFundingOrgIdAndGgisReferenceNum(int fundingOrgId, String ggisReferenceNumber) {
		final List<Submission> submissions = submissionRepository.findBySchemeFunderIdAndSchemeGgisIdentifier(fundingOrgId, ggisReferenceNumber);
		return mapSubmissionsToSubmissionListDto(submissions);
	}

	public SubmissionListDTO getSubmissionsByFundingOrgId(int fundingOrgId) {
		final List<Submission> submissions = submissionRepository.findBySchemeFunderId(fundingOrgId);
		return mapSubmissionsToSubmissionListDto(submissions);
	}

	private SubmissionListDTO mapSubmissionsToSubmissionListDto(final List<Submission> submissions) {
		return SubmissionListDTO
				.builder()
				.numberOfResults(submissions.size())
				.submissions(
						submissions
						.stream()
						.map(submissionMapper::submissionToSubmissionDto)
						.collect(Collectors.collectingAndThen(Collectors.toList(), result -> {
							if (result.isEmpty())
								throw new SubmissionNotFoundException("No submissions found");
							return result;
						})))
				.build();
	}
}

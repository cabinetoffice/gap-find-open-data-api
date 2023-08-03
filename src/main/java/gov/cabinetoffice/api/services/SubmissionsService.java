package gov.cabinetoffice.api.services;

import gov.cabinetoffice.api.dtos.submission.ApplicationListDTO;
import gov.cabinetoffice.api.entities.Submission;
import gov.cabinetoffice.api.mappers.SubmissionMapper;
import gov.cabinetoffice.api.repositories.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionsService {

	private final SubmissionRepository submissionRepository;
	private final SubmissionMapper submissionMapper;

	public ApplicationListDTO getSubmissionsByFundingOrgIdAndGgisReferenceNum(int fundingOrgId, String ggisReferenceNumber) {
		final List<Submission> submissions = submissionRepository.findBySchemeFunderIdAndSchemeGgisIdentifier(fundingOrgId, ggisReferenceNumber);
		return submissionMapper.submissionListToApplicationListDto(submissions);
	}

	public ApplicationListDTO getSubmissionsByFundingOrgId(int fundingOrgId) {
		final List<Submission> submissions = submissionRepository.findBySchemeFunderId(fundingOrgId);
		return submissionMapper.submissionListToApplicationListDto(submissions);
	}
}

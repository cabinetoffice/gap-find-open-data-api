package gov.cabinetoffice.api.prototype.services;

import gov.cabinetoffice.api.prototype.dtos.submission.SubmissionDTO;
import gov.cabinetoffice.api.prototype.dtos.submission.SubmissionListDTO;
import gov.cabinetoffice.api.prototype.exceptions.SubmissionNotFoundException;
import gov.cabinetoffice.api.prototype.mappers.SubmissionMapper;
import gov.cabinetoffice.api.prototype.repositories.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissionsService {

	private final SubmissionRepository submissionRepository;

	private final SubmissionMapper submissionMapper;

	public SubmissionListDTO getSubmissionByApplicationId(int applicationId) {

		final List<SubmissionDTO> submissionDTOS = submissionRepository
			.findByApplicationGrantApplicationId(applicationId)
			.stream()
			.map(submissionMapper::submissionToSubmissionDto)
			.collect(Collectors.collectingAndThen(Collectors.toList(), result -> {
				if (result.isEmpty())
					throw new SubmissionNotFoundException("No submissions found with application id " + applicationId);
				return result;
			}));

		return SubmissionListDTO.builder().submissions(submissionDTOS).build();
	}

}

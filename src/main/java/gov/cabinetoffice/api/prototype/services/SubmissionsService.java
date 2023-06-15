package gov.cabinetoffice.api.prototype.services;

import gov.cabinetoffice.api.prototype.dtos.submission.SubmissionDTO;
import gov.cabinetoffice.api.prototype.entities.Submission;
import gov.cabinetoffice.api.prototype.exceptions.SubmissionNotFoundException;
import gov.cabinetoffice.api.prototype.mappers.SubmissionMapper;
import gov.cabinetoffice.api.prototype.repositories.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionsService {

    private final SubmissionRepository submissionRepository;

    private final SubmissionMapper submissionMapper;

    public List<SubmissionDTO> getSubmissionByApplicationId(Integer applicationId) {
        List<Submission> submissions = submissionRepository.findByApplicationGrantApplicationId(applicationId);
        if (submissions.isEmpty()) {
            throw new SubmissionNotFoundException("No submissions found with application id " + applicationId);
        }
        else {
            List<SubmissionDTO> submissionDTOList = new ArrayList<>();
            for (Submission submission : submissions) {
                submissionDTOList.add(submissionMapper.submissionToSubmissionDto(submission));
            }
            return submissionDTOList;
        }
    }

}

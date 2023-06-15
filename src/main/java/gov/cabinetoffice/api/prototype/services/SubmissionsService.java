package gov.cabinetoffice.api.prototype.services;

import gov.cabinetoffice.api.prototype.entities.Submission;
import gov.cabinetoffice.api.prototype.exceptions.SubmissionNotFoundException;
import gov.cabinetoffice.api.prototype.repositories.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionsService {

    private final SubmissionRepository submissionRepository;

    public List<Submission> getSubmissionByApplicationId(Integer applicationId) {
        List<Submission> submissions = submissionRepository.findByApplicationGrantApplicationId(applicationId);
        if (submissions.isEmpty()) {
            throw new SubmissionNotFoundException("No submissions found with application id " + applicationId);
        }
        else {
            return submissions;
        }
    }

}

package gov.cabinetoffice.api.prototype.services;

import gov.cabinetoffice.api.prototype.entities.Submission;
import gov.cabinetoffice.api.prototype.repositories.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionsService {

    private final SubmissionRepository submissionRepository;

    public List<Submission> getSubmissionByApplicationId(Integer applicationId) {
        return this.submissionRepository.findByApplicationGrantApplicationId(applicationId);
    }

}

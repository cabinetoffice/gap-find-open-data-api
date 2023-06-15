package gov.cabinetoffice.api.prototype.services;

import gov.cabinetoffice.api.prototype.entities.ApplicationFormEntity;
import gov.cabinetoffice.api.prototype.exceptions.ApplicationFormNotFoundException;
import gov.cabinetoffice.api.prototype.repositories.ApplicationFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationFormService {

    private final ApplicationFormRepository applicationFormRepository;

    public ApplicationFormEntity getApplicationById(Integer applicationId) {
        return this.applicationFormRepository.findById(applicationId).orElseThrow(
                () -> new ApplicationFormNotFoundException("No application found with id " + applicationId));
    }

}

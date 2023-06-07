package gov.cabinetoffice.api.prototype.services;

import gov.cabinetoffice.api.prototype.entities.ApplicationFormEntity;
import gov.cabinetoffice.api.prototype.exceptions.ApplicationFormException;
import gov.cabinetoffice.api.prototype.repositories.ApplicationFormRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationFormService {

    private final ApplicationFormRepository applicationFormRepository;

    public ApplicationFormEntity getApplicationById(Integer applicationId) {
        return this.applicationFormRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationFormException("No application found with id " + applicationId));
    }

}

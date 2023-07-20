package gov.cabinetoffice.api.services;

import gov.cabinetoffice.api.entities.ApplicationFormEntity;
import gov.cabinetoffice.api.exceptions.ApplicationFormNotFoundException;
import gov.cabinetoffice.api.repositories.ApplicationFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationFormService {

	private final ApplicationFormRepository applicationFormRepository;

	public ApplicationFormEntity getApplicationById(int applicationId) {
		return this.applicationFormRepository.findById(applicationId)
			.orElseThrow(() -> new ApplicationFormNotFoundException("No application found with id " + applicationId));
	}

}

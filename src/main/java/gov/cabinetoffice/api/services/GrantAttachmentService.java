package gov.cabinetoffice.api.services;

import gov.cabinetoffice.api.entities.GrantAttachment;
import gov.cabinetoffice.api.exceptions.GrantAttachmentNotFoundException;
import gov.cabinetoffice.api.repositories.GrantAttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GrantAttachmentService {

	private final GrantAttachmentRepository grantAttachmentRepository;

	public GrantAttachment getGrantAttachmentById(UUID id) {
		return grantAttachmentRepository.findById(id)
			.orElseThrow(() -> new GrantAttachmentNotFoundException("No grant attachment found with id " + id));
	}

}

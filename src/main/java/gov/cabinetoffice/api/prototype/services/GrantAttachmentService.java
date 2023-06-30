package gov.cabinetoffice.api.prototype.services;

import gov.cabinetoffice.api.prototype.entities.GrantAttachment;
import gov.cabinetoffice.api.prototype.exceptions.GrantAttachmentNotFoundException;
import gov.cabinetoffice.api.prototype.repositories.GrantAttachmentRepository;
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

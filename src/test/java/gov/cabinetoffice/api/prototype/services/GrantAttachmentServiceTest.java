package gov.cabinetoffice.api.prototype.services;

import gov.cabinetoffice.api.prototype.entities.GrantAttachment;
import gov.cabinetoffice.api.prototype.exceptions.GrantAttachmentNotFoundException;
import gov.cabinetoffice.api.prototype.repositories.GrantAttachmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GrantAttachmentServiceTest {

	@Mock
	private GrantAttachmentRepository grantAttachmentRepository;

	@InjectMocks
	private GrantAttachmentService grantAttachmentService;

	private final UUID GRANT_ATTACHMENT_ID = UUID.randomUUID();

	@Test
	void getGrantAttachmentById_found() {
		final GrantAttachment grantAttachment = GrantAttachment.builder().id(GRANT_ATTACHMENT_ID).build();

		when(grantAttachmentRepository.findById(GRANT_ATTACHMENT_ID)).thenReturn(ofNullable(grantAttachment));

		final GrantAttachment response = grantAttachmentService.getGrantAttachmentById(GRANT_ATTACHMENT_ID);

		verify(grantAttachmentRepository).findById(GRANT_ATTACHMENT_ID);
		assertThat(response).isEqualTo(grantAttachment);
	}

	@Test
	void getGrantAttachmentById_notFound() {
		when(grantAttachmentRepository.findById(GRANT_ATTACHMENT_ID))
				.thenThrow(new GrantAttachmentNotFoundException("Grant attachment not found for attachment id " + GRANT_ATTACHMENT_ID));
		final Throwable exception = assertThrows(GrantAttachmentNotFoundException.class,
				() -> grantAttachmentService.getGrantAttachmentById(GRANT_ATTACHMENT_ID));

		verify(grantAttachmentRepository).findById(GRANT_ATTACHMENT_ID);
		assertThat(exception.getMessage()).isEqualTo("Grant attachment not found for attachment id " + GRANT_ATTACHMENT_ID);
	}

}
package gov.cabinetoffice.api.prototype.services;

import gov.cabinetoffice.api.prototype.dtos.submission.SubmissionDTO;
import gov.cabinetoffice.api.prototype.dtos.submission.SubmissionListDTO;
import gov.cabinetoffice.api.prototype.entities.*;
import gov.cabinetoffice.api.prototype.exceptions.GrantAttachmentNotFoundException;
import gov.cabinetoffice.api.prototype.exceptions.SubmissionNotFoundException;
import gov.cabinetoffice.api.prototype.mappers.SubmissionMapper;
import gov.cabinetoffice.api.prototype.mappers.SubmissionMapperImpl;
import gov.cabinetoffice.api.prototype.repositories.GrantAttachmentRepository;
import gov.cabinetoffice.api.prototype.repositories.SubmissionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static gov.cabinetoffice.api.prototype.test_data_generator.RandomSubmissionGenerator.randomSubmission;
import static gov.cabinetoffice.api.prototype.test_data_generator.RandomSubmissionGenerator.randomSubmissionDefinition;
import static java.util.Optional.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
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
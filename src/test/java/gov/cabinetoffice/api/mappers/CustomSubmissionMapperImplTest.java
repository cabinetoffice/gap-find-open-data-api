package gov.cabinetoffice.api.mappers;

import gov.cabinetoffice.api.config.S3ConfigProperties;
import gov.cabinetoffice.api.dtos.submission.SubmissionDTO;
import gov.cabinetoffice.api.dtos.submission.SubmissionSectionDTO;
import gov.cabinetoffice.api.entities.GrantAttachment;
import gov.cabinetoffice.api.entities.Submission;
import gov.cabinetoffice.api.exceptions.UserNotFoundException;
import gov.cabinetoffice.api.models.submission.SubmissionQuestion;
import gov.cabinetoffice.api.models.submission.SubmissionSection;
import gov.cabinetoffice.api.services.GrantAttachmentService;
import gov.cabinetoffice.api.services.S3Service;
import gov.cabinetoffice.api.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static gov.cabinetoffice.api.enums.GrantAttachmentStatus.AVAILABLE;
import static gov.cabinetoffice.api.enums.GrantAttachmentStatus.AWAITING_SCAN;
import static gov.cabinetoffice.api.enums.GrantAttachmentStatus.QUARANTINED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomSubmissionMapperImplTest {

    @Mock
    private GrantAttachmentService grantAttachmentService;

    @Mock
    private S3Service s3Service;

    @Mock
    private S3ConfigProperties s3ConfigProperties;

    @Mock
    private UserService userService;

    @InjectMocks
    private CustomSubmissionMapperImpl customSubmissionMapperImpl;

    @Test
    void buildUploadResponse_AttachmentHasAwaitingScanStatus() {
        final UUID grantAttachmentId = UUID.randomUUID();
        final String expectedResult = "The URL for this file is not available at the moment. It is undergoing our antivirus process. Please try later.";

        final SubmissionQuestion submissionQuestion = SubmissionQuestion.builder()
                .attachmentId(grantAttachmentId)
                .build();

        final GrantAttachment grantAttachment = GrantAttachment.builder()
                .id(grantAttachmentId)
                .status(AWAITING_SCAN)
                .build();

        when(grantAttachmentService.getGrantAttachmentById(grantAttachmentId)).thenReturn(grantAttachment);

        final String result = customSubmissionMapperImpl.buildUploadResponse(submissionQuestion);
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void buildUploadResponse_AttachmentHasQuarantinedStatus() {
        final UUID grantAttachmentId = UUID.randomUUID();
        final String expectedResult = "This file is not available. Please contact the Grant Applicant.";

        final SubmissionQuestion submissionQuestion = SubmissionQuestion.builder()
                .attachmentId(grantAttachmentId)
                .build();

        final GrantAttachment grantAttachment = GrantAttachment.builder()
                .id(grantAttachmentId)
                .status(QUARANTINED)
                .build();

        when(grantAttachmentService.getGrantAttachmentById(grantAttachmentId)).thenReturn(grantAttachment);

        final String result = customSubmissionMapperImpl.buildUploadResponse(submissionQuestion);
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void buildUploadResponse_AttachmentHasURI() {
        final UUID grantAttachmentId = UUID.randomUUID();
        final String expectedResult = "presignedUrl";

        final SubmissionQuestion submissionQuestion = SubmissionQuestion.builder()
                .attachmentId(grantAttachmentId)
                .build();

        final GrantAttachment grantAttachment = GrantAttachment.builder()
                .id(grantAttachmentId)
                .status(AVAILABLE)
                .location("bucket/location")
                .build();

        when(s3ConfigProperties.getSourceBucket()).thenReturn("bucket");
        when(grantAttachmentService.getGrantAttachmentById(grantAttachmentId)).thenReturn(grantAttachment);
        when(s3Service.createPresignedURL(any(), any())).thenReturn(expectedResult);

        final String result = customSubmissionMapperImpl.buildUploadResponse(submissionQuestion);
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void submissionToSubmissionDto() {
        final Submission submission = SubmissionMapperTestData.getSubmission();
        final SubmissionDTO submissionDto = SubmissionMapperTestData.getSubmissionDto();

        when(userService.getUserEmailForSub(SubmissionMapperTestData.APPLICANT_USER_ID)).thenReturn(SubmissionMapperTestData.GRANT_APPLICANT_EMAIL_ADDRESS);

        final SubmissionDTO result = customSubmissionMapperImpl.submissionToSubmissionDto(submission);

        assertThat(result).isEqualTo(submissionDto);
    }

    @Test
    void submissionToSubmissionDto_UserNotFoundException() {
        final Submission submission = SubmissionMapperTestData.getSubmission();
        final SubmissionDTO submissionDto = SubmissionMapperTestData.getSubmissionDto();
        submissionDto.setGrantApplicantEmailAddress("User not found");
        when(userService.getUserEmailForSub(SubmissionMapperTestData.APPLICANT_USER_ID)).thenThrow(new UserNotFoundException("User not found"));

        final SubmissionDTO result = customSubmissionMapperImpl.submissionToSubmissionDto(submission);

        assertThat(result).isEqualTo(submissionDto);
    }

    @Test
    void submissionToSubmissionDto_returnNullIfSubmissionIsNull() {
        final SubmissionDTO result = customSubmissionMapperImpl.submissionToSubmissionDto(null);
        assertThat(result).isNull();
    }

    @Test
    void submissionSectionToSubmissionSectionDto() {
        final SubmissionSection section = SubmissionMapperTestData.getSubmissionSection();
        final SubmissionSectionDTO sectionDTO = SubmissionMapperTestData.getSubmissionSectionDto();

        final SubmissionSectionDTO result = customSubmissionMapperImpl
                .submissionSectionToSubmissionSectionDto(section);

        assertThat(result).isEqualTo(sectionDTO);
    }

    @Test
    void submissionSectionToSubmissionSectionDto_returnNullIfSubmissionIsNull() {
        final SubmissionSectionDTO result = customSubmissionMapperImpl.submissionSectionToSubmissionSectionDto(null);
        assertThat(result).isNull();
    }

    @Test
    void submissionApplicationApplicationName() {
        final Submission submission = SubmissionMapperTestData.getSubmission();

        final String result = customSubmissionMapperImpl.submissionApplicationApplicationName(submission);

        assertThat(result).isEqualTo(submission.getApplication().getApplicationName());
    }

    @Test
    void submissionApplicationApplicationName_returnNullIfSubmissionIsNull() {
        final String result = customSubmissionMapperImpl.submissionApplicationApplicationName(null);
        assertThat(result).isNull();
    }

    @Test
    void submissionApplicationApplicationName_returnNullIfSubmissionApplicationIsNull() {
        final Submission submission = Submission.builder().application(null).build();
        final String result = customSubmissionMapperImpl.submissionApplicationApplicationName(submission);
        assertThat(result).isNull();
    }

    @Test
    void submissionSchemeEmail() {
        final Submission submission = SubmissionMapperTestData.getSubmission();

        final String result = customSubmissionMapperImpl.submissionSchemeEmail(submission);

        assertThat(result).isEqualTo(submission.getScheme().getEmail());
    }

    @Test
    void submissionSchemeEmail_returnNullIfSubmissionIsNull() {
        final String result = customSubmissionMapperImpl.submissionSchemeEmail(null);
        assertThat(result).isNull();
    }

    @Test
    void submissionSchemeEmail_returnNullIfSubmissionSchemeIsNull() {
        final Submission submission = Submission.builder().scheme(null).build();
        final String result = customSubmissionMapperImpl.submissionSchemeEmail(submission);
        assertThat(result).isNull();
    }

    @Test
    void submissionSchemeGgisIdentifier() {
        final Submission submission = SubmissionMapperTestData.getSubmission();

        final String result = customSubmissionMapperImpl.submissionSchemeGgisIdentifier(submission);

        assertThat(result).isEqualTo(submission.getScheme().getGgisIdentifier());
    }

    @Test
    void submissionSchemeGgisIdentifier_returnNullIfSubmissionIsNull() {
        final String result = customSubmissionMapperImpl.submissionSchemeGgisIdentifier(null);
        assertThat(result).isNull();
    }

    @Test
    void submissionSchemeGgisIdentifier_returnNullIfSubmissionSchemeIsNull() {
        final Submission submission = Submission.builder().scheme(null).build();
        final String result = customSubmissionMapperImpl.submissionSchemeGgisIdentifier(submission);
        assertThat(result).isNull();
    }

    @Test
    void submissionDefinitionSections() {
        final Submission submission = SubmissionMapperTestData.getSubmission();

        final List<SubmissionSection> result = customSubmissionMapperImpl.submissionDefinitionSections(submission);

        assertThat(result).isEqualTo(submission.getDefinition().getSections());
    }

    @Test
    void submissionDefinitionSections_returnNullIfSubmissionIsNull() {
        final List<SubmissionSection> result = customSubmissionMapperImpl.submissionDefinitionSections(null);
        assertThat(result).isNull();
    }

    @Test
    void submissionDefinitionSections_returnNullIfSubmissionDefinitionIsNull() {
        final Submission submission = Submission.builder().definition(null).build();
        final List<SubmissionSection> result = customSubmissionMapperImpl.submissionDefinitionSections(submission);
        assertThat(result).isNull();
    }
}
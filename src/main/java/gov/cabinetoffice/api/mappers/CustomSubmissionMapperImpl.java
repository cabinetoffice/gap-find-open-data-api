package gov.cabinetoffice.api.mappers;

import gov.cabinetoffice.api.config.S3ConfigProperties;
import gov.cabinetoffice.api.dtos.submission.AddressDTO;
import gov.cabinetoffice.api.dtos.submission.SubmissionDTO;
import gov.cabinetoffice.api.dtos.submission.SubmissionQuestionDTO;
import gov.cabinetoffice.api.dtos.submission.SubmissionSectionDTO;
import gov.cabinetoffice.api.entities.GrantAttachment;
import gov.cabinetoffice.api.entities.Submission;
import gov.cabinetoffice.api.exceptions.UserNotFoundException;
import gov.cabinetoffice.api.models.submission.SubmissionQuestion;
import gov.cabinetoffice.api.models.submission.SubmissionSection;
import gov.cabinetoffice.api.services.GrantAttachmentService;
import gov.cabinetoffice.api.services.S3Service;
import gov.cabinetoffice.api.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static gov.cabinetoffice.api.enums.GrantAttachmentStatus.AWAITING_SCAN;
import static gov.cabinetoffice.api.enums.GrantAttachmentStatus.QUARANTINED;

@RequiredArgsConstructor
@Component
@Primary
@Slf4j
public class CustomSubmissionMapperImpl implements SubmissionMapper {

    private final GrantAttachmentService grantAttachmentService;
    private final S3Service s3Service;
    private final S3ConfigProperties s3ConfigProperties;
    private final UserService userService;

    @Override
    public List<SubmissionSectionDTO> mapSections(List<SubmissionSection> sections) {
        return SubmissionMapper.super.mapSections(sections);
    }

    @Override
    public List<SubmissionSectionDTO> submissionSectionListToSubmissionSectionDtoList(
            List<SubmissionSection> submissionSections) {
        return SubmissionMapper.super.submissionSectionListToSubmissionSectionDtoList(submissionSections);
    }

    @Override
    public List<SubmissionQuestionDTO> submissionQuestionListToSubmissionQuestionDtoList(
            List<SubmissionQuestion> submissionQuestions) {
        return SubmissionMapper.super.submissionQuestionListToSubmissionQuestionDtoList(submissionQuestions);
    }

    @Override
    public SubmissionQuestionDTO submissionQuestionToSubmissionQuestionDto(SubmissionQuestion submissionQuestion) {
        return SubmissionMapper.super.submissionQuestionToSubmissionQuestionDto(submissionQuestion);
    }

    @Override
    public Object getQuestionResponseByResponseType(SubmissionQuestion submissionQuestion) {
        return SubmissionMapper.super.getQuestionResponseByResponseType(submissionQuestion);
    }

    @Override
    public String buildUploadResponse(SubmissionQuestion submissionQuestion) {
        final UUID grantAttachmentId = submissionQuestion.getAttachmentId();
        final GrantAttachment grantAttachment = grantAttachmentService.getGrantAttachmentById(grantAttachmentId);
        final String bucketName = s3ConfigProperties.getSourceBucket();

        if (grantAttachment.getStatus().equals(QUARANTINED)) {
            return "This file is not available please contact the Grant Applicant";
        }

        if (grantAttachment.getStatus().equals(AWAITING_SCAN)) {
            return "The url for this file is not available at the moment. It is undergoing our antivirus process. Please try later";
        }

        final String objectKey = grantAttachment.getLocation().split(s3ConfigProperties.getSourceBucket() + "/")[1];

        return s3Service.createPresignedURL(bucketName, objectKey);
    }

    @Override
    public LocalDate buildDate(String[] multiResponse) {
        return SubmissionMapper.super.buildDate(multiResponse);
    }

    @Override
    public AddressDTO buildAddress(String[] multiResponse) {
        return SubmissionMapper.super.buildAddress(multiResponse);
    }

    @Override
    public SubmissionDTO submissionToSubmissionDto(Submission submission) {
        if (submission == null) {
            return null;
        }

        final List<SubmissionSection> sections = submissionDefinitionSections(submission);
        return SubmissionDTO.builder()
                .submissionId(submission.getId())
                .grantApplicantEmailAddress(getUserEmail(submission.getApplicant().getUserId()))
                .submittedTimeStamp(submission.getSubmittedDate())
                .sections(mapSections(sections))
                .build();
    }

    @Override
    public SubmissionSectionDTO submissionSectionToSubmissionSectionDto(SubmissionSection submissionSection) {
        if (submissionSection == null) {
            return null;
        }

        return SubmissionSectionDTO.builder()
                .sectionId(submissionSection.getSectionId())
                .sectionTitle(submissionSection.getSectionTitle())
                .questions(submissionQuestionListToSubmissionQuestionDtoList(submissionSection.getQuestions()))
                .build();

    }

    public String submissionApplicationApplicationName(Submission submission) {
        return (submission != null && submission.getApplication() != null)
                ? submission.getApplication().getApplicationName() : null;
    }

    public String submissionSchemeEmail(Submission submission) {
        return (submission != null && submission.getScheme() != null) ? submission.getScheme().getEmail() : null;
    }

    public String submissionSchemeGgisIdentifier(Submission submission) {
        return (submission != null && submission.getScheme() != null) ? submission.getScheme().getGgisIdentifier()
                : null;
    }

    public List<SubmissionSection> submissionDefinitionSections(Submission submission) {
        return (submission != null && submission.getDefinition() != null) ? submission.getDefinition().getSections()
                : null;
    }

    private String getUserEmail(String sub) {
        try {
            return userService.getUserEmailForSub(sub);
        } catch (UserNotFoundException e) {
            log.error("User not found for sub {}", sub);
            return "User not found";
        }
    }

}

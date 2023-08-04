package gov.cabinetoffice.api.mappers;

import gov.cabinetoffice.api.config.S3ConfigProperties;
import gov.cabinetoffice.api.dtos.submission.*;
import gov.cabinetoffice.api.entities.ApplicationFormEntity;
import gov.cabinetoffice.api.entities.GrantAttachment;
import gov.cabinetoffice.api.entities.Submission;
import gov.cabinetoffice.api.exceptions.SubmissionNotFoundException;
import gov.cabinetoffice.api.models.submission.SubmissionQuestion;
import gov.cabinetoffice.api.models.submission.SubmissionSection;
import gov.cabinetoffice.api.services.GrantAttachmentService;
import gov.cabinetoffice.api.services.S3Service;
import lombok.RequiredArgsConstructor;
import org.javatuples.Pair;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Primary
public class CustomSubmissionMapperImpl implements SubmissionMapper {

	public static final String AMAZON_AWS_URL = ".amazonaws.com/";
	private final GrantAttachmentService grantAttachmentService;
	private final S3Service s3Service;
	private final S3ConfigProperties s3ConfigProperties;

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
		final String objectKey = grantAttachment.getLocation().split(AMAZON_AWS_URL)[1];
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

	// TODO - look into whether this is horrendously inefficient. Potentially more performant to do all this looping at query level.
	@Override
	public ApplicationListDTO submissionListToApplicationListDto(final List<Submission> submissions) {
		final List<ApplicationDTO> applicationSubmissions = Optional.ofNullable(submissions)
				.orElse(Collections.emptyList())
				.stream()
				.map(Submission::getApplication)
				.distinct()
				.toList()
				.stream()
				.map(form -> groupSubmissionsByApplicationForm(form, submissions))
				.toList();

		return ApplicationListDTO.builder()
				.numberOfResults(applicationSubmissions.size())
				.applications(applicationSubmissions)
				.build();
	}

	private ApplicationDTO groupSubmissionsByApplicationForm(final ApplicationFormEntity form, final List<Submission> submissions) {
		final Pair<String, String> applicationDetails = submissions.stream()
				.filter(submission -> form.getGrantApplicationId().equals(submission.getApplication().getGrantApplicationId()))
				.findFirst()
				.map(submission ->
						Pair.with(
								submission.getScheme().getEmail(),
								submission.getScheme().getGgisIdentifier()
						)
				)
				.orElseThrow(() -> new SubmissionNotFoundException("No submissions found"));

		final List<SubmissionDTO> submissionDtos = submissions.stream()
				.filter(submission -> form.getGrantApplicationId().equals(submission.getApplication().getGrantApplicationId()))
				.map(this::submissionToSubmissionDto)
				.toList();

		return ApplicationDTO.builder()
				.grantAdminEmailAddress(applicationDetails.getValue0())
				.ggisReferenceNumber(applicationDetails.getValue1())
				.applicationFormName(form.getApplicationName())
				.submissions(submissionDtos)
				.build();
	}

	@Override
	public SubmissionDTO submissionToSubmissionDto(Submission submission) {
		if (submission == null) {
			return null;
		}

		final List<SubmissionSection> sections = submissionDefinitionSections(submission);
		return SubmissionDTO.builder()
			.submissionId(submission.getId())
			.grantApplicantEmailAddress(submissionSchemeEmail(submission))
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

}

package gov.cabinetoffice.api.prototype.mappers;

import gov.cabinetoffice.api.prototype.dtos.submission.AddressDTO;
import gov.cabinetoffice.api.prototype.dtos.submission.SubmissionDTO;
import gov.cabinetoffice.api.prototype.dtos.submission.SubmissionQuestionDTO;
import gov.cabinetoffice.api.prototype.dtos.submission.SubmissionSectionDTO;
import gov.cabinetoffice.api.prototype.entities.ApplicationFormEntity;
import gov.cabinetoffice.api.prototype.entities.GrantAttachment;
import gov.cabinetoffice.api.prototype.entities.SchemeEntity;
import gov.cabinetoffice.api.prototype.entities.Submission;
import gov.cabinetoffice.api.prototype.models.submission.SubmissionDefinition;
import gov.cabinetoffice.api.prototype.models.submission.SubmissionQuestion;
import gov.cabinetoffice.api.prototype.models.submission.SubmissionSection;
import gov.cabinetoffice.api.prototype.services.GrantAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@Primary
public class CustomSubmissionMapperImpl implements SubmissionMapper {

	@Autowired
	GrantAttachmentService grantAttachmentService;

	@Autowired
	SubmissionMapperImpl submissionMapperImpl;

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
		UUID grantAttachmentId = submissionQuestion.getAttachmentId();
		GrantAttachment grantAttachment = grantAttachmentService.getGrantAttachmentById(grantAttachmentId);
		return grantAttachment.getLocation();
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

		SubmissionDTO.SubmissionDTOBuilder submissionDTO = SubmissionDTO.builder();

		submissionDTO.submissionId(submission.getId());
		submissionDTO.applicationFormName(submissionApplicationApplicationName(submission));
		submissionDTO.grantAdminEmailAddress(submissionSchemeEmail(submission));
		submissionDTO.grantApplicantEmailAddress(submissionSchemeEmail(submission));
		submissionDTO.ggisReferenceNumber(submissionSchemeGgisIdentifier(submission));
		submissionDTO.submittedTimeStamp(submission.getSubmittedDate());
		List<SubmissionSection> sections = submissionDefinitionSections(submission);
		submissionDTO.sections(mapSections(sections));

		return submissionDTO.build();
	}

	@Override
	public SubmissionSectionDTO submissionSectionToSubmissionSectionDto(SubmissionSection submissionSection) {
		if (submissionSection == null) {
			return null;
		}

		SubmissionSectionDTO.SubmissionSectionDTOBuilder submissionSectionDTO = SubmissionSectionDTO.builder();

		submissionSectionDTO.sectionId(submissionSection.getSectionId());
		submissionSectionDTO.sectionTitle(submissionSection.getSectionTitle());
		submissionSectionDTO
			.questions(submissionQuestionListToSubmissionQuestionDtoList(submissionSection.getQuestions()));

		return submissionSectionDTO.build();
	}

	private String submissionApplicationApplicationName(Submission submission) {
		if (submission == null)
			return null;
		ApplicationFormEntity application = submission.getApplication();

		if (application == null)
			return null;
		
		return application.getApplicationName();
	}

	private String submissionSchemeEmail(Submission submission) {
		if (submission == null) {
			return null;
		}
		SchemeEntity scheme = submission.getScheme();
		if (scheme == null) {
			return null;
		}
		return scheme.getEmail();
	}

	private String submissionSchemeGgisIdentifier(Submission submission) {
		if (submission == null) {
			return null;
		}
		SchemeEntity scheme = submission.getScheme();
		if (scheme == null) {
			return null;
		}
		return scheme.getGgisIdentifier();
	}

	private List<SubmissionSection> submissionDefinitionSections(Submission submission) {
		if (submission == null) {
			return null;
		}
		SubmissionDefinition definition = submission.getDefinition();
		if (definition == null) {
			return null;
		}

		return definition.getSections();
	}

}
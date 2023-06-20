package gov.cabinetoffice.api.prototype.mappers;

import gov.cabinetoffice.api.prototype.dtos.submission.AddressDTO;
import gov.cabinetoffice.api.prototype.dtos.submission.SubmissionDTO;
import gov.cabinetoffice.api.prototype.dtos.submission.SubmissionQuestionDTO;
import gov.cabinetoffice.api.prototype.dtos.submission.SubmissionSectionDTO;
import gov.cabinetoffice.api.prototype.entities.Submission;
import gov.cabinetoffice.api.prototype.models.submission.SubmissionQuestion;
import gov.cabinetoffice.api.prototype.models.submission.SubmissionSection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.util.List;

import static java.lang.Integer.parseInt;

@Mapper(componentModel = "spring", uses = { CustomSubmissionMapperImpl.class })
public interface SubmissionMapper {

	@Mapping(source = "id", target = "submissionId")
	@Mapping(source = "application.applicationName", target = "applicationFormName")
	// TODO is adminEmail the same as scheme.email? Don't store grantApplicant email
	@Mapping(source = "scheme.email", target = "grantAdminEmailAddress")
	@Mapping(source = "scheme.email", target = "grantApplicantEmailAddress")
	@Mapping(source = "scheme.ggisIdentifier", target = "ggisReferenceNumber")
	@Mapping(source = "submittedDate", target = "submittedTimeStamp")
	@Mapping(source = "definition.sections", target = "sections", qualifiedByName = "mapSections")
	SubmissionDTO submissionToSubmissionDto(Submission submission);

	@Named("mapSections")
	default List<SubmissionSectionDTO> mapSections(List<SubmissionSection> sections) {
		return submissionSectionListToSubmissionSectionDtoList(sections);
	}

	@Mapping(source = "sectionId", target = "sectionId")
	@Mapping(source = "sectionTitle", target = "sectionTitle")
	@Mapping(target = "questions", qualifiedByName = "mapQuestions")
	SubmissionSectionDTO submissionSectionToSubmissionSectionDto(SubmissionSection submissionSection);

	default List<SubmissionSectionDTO> submissionSectionListToSubmissionSectionDtoList(
			List<SubmissionSection> submissionSections) {
		return submissionSections.stream().map(this::submissionSectionToSubmissionSectionDto).toList();
	}

	@Named("mapQuestions")
	default List<SubmissionQuestionDTO> submissionQuestionListToSubmissionQuestionDtoList(
			List<SubmissionQuestion> submissionQuestions) {
		return submissionQuestions.stream().map(this::submissionQuestionToSubmissionQuestionDto).toList();
	}

	default SubmissionQuestionDTO submissionQuestionToSubmissionQuestionDto(SubmissionQuestion submissionQuestion) {
		Object questionResponse;
		final String response = submissionQuestion.getResponse();
		final String[] multiResponse = submissionQuestion.getMultiResponse();
		final SubmissionQuestionDTO submissionQuestionDTO = SubmissionQuestionDTO.builder()
			.questionId(submissionQuestion.getQuestionId())
			.questionTitle(submissionQuestion.getFieldTitle())
			.build();
		if (response == null && multiResponse == null)
			return submissionQuestionDTO;

		questionResponse = getQuestionResponseByResponseType(submissionQuestion);

		submissionQuestionDTO.setQuestionResponse(questionResponse);
		return submissionQuestionDTO;
	}

	default Object getQuestionResponseByResponseType(SubmissionQuestion submissionQuestion) {
		final String response = submissionQuestion.getResponse();
		final String[] multiResponse = submissionQuestion.getMultiResponse();
		return switch (submissionQuestion.getResponseType()) {
			case YesNo, Dropdown, ShortAnswer, LongAnswer, Numeric -> response;
			case MultipleSelection -> multiResponse;
			case AddressInput -> buildAddress(multiResponse);
			case Date -> buildDate(multiResponse);
			case SingleFileUpload -> buildUploadResponse(submissionQuestion);
			default -> ""; // TODO do we want this to be null or an empty string?
		};
	}

	default String buildUploadResponse(SubmissionQuestion submissionQuestion) {
		return "";
	};

	default LocalDate buildDate(String[] multiResponse) {
		return LocalDate.of(parseInt(multiResponse[2]), parseInt(multiResponse[1]), parseInt(multiResponse[0]));
	}

	default AddressDTO buildAddress(String[] multiResponse) {
		return AddressDTO.builder()
			.addressLine1(multiResponse[0])
			.addressLine2(multiResponse[1])
			.town(multiResponse[2])
			.county(multiResponse[3])
			.postcode(multiResponse[4])
			.build();
	}

}

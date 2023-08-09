package gov.cabinetoffice.api.mappers;

import gov.cabinetoffice.api.dtos.submission.*;
import gov.cabinetoffice.api.entities.Submission;
import gov.cabinetoffice.api.models.submission.SubmissionQuestion;
import gov.cabinetoffice.api.models.submission.SubmissionSection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.util.List;

import static java.lang.Integer.parseInt;

@Mapper(componentModel = "spring", uses = { CustomSubmissionMapperImpl.class })
public interface SubmissionMapper {

	@Mapping(source = "id", target = "submissionId")
	@Mapping(source = "scheme.email", target = "grantApplicantEmailAddress") //TODO fetch the applicant email from the database after one login work completes - this is not the correct value
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
			default -> "";
		};
	}

	// returns empty string, so it won't be overridden by auto generated
	// SubmissionMapperImpl
	default String buildUploadResponse(SubmissionQuestion submissionQuestion) {
		return "";
	}

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

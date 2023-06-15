package gov.cabinetoffice.api.prototype.mappers;

import gov.cabinetoffice.api.prototype.dtos.submission.AddressDTO;
import gov.cabinetoffice.api.prototype.dtos.submission.SubmissionDTO;
import gov.cabinetoffice.api.prototype.dtos.submission.SubmissionQuestionDTO;
import gov.cabinetoffice.api.prototype.dtos.submission.SubmissionSectionDTO;
import gov.cabinetoffice.api.prototype.entities.Submission;
import gov.cabinetoffice.api.prototype.enums.ResponseTypeEnum;
import gov.cabinetoffice.api.prototype.models.submission.SubmissionQuestion;
import gov.cabinetoffice.api.prototype.models.submission.SubmissionSection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;

import javax.print.attribute.DateTimeSyntax;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.*;

@Mapper(componentModel = "spring")
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
        List<SubmissionSectionDTO> submissionSectionDTOs = new ArrayList<>();
        for (SubmissionSection section : submissionSections) {
            submissionSectionDTOs.add(submissionSectionToSubmissionSectionDto(section));
        }
        return submissionSectionDTOs;
    }

    @Named("mapQuestions")
    default List<SubmissionQuestionDTO> submissionQuestionListToSubmissionQuestionDtoList(
            List<SubmissionQuestion> submissionQuestions) {
        List<SubmissionQuestionDTO> submissionQuestionDTOList = new ArrayList<>();
        for (SubmissionQuestion submissionQuestion : submissionQuestions) {

            submissionQuestionDTOList.add(submissionQuestionToSubmissionQuestionDto(submissionQuestion));
        }
        return submissionQuestionDTOList;
    }

    default SubmissionQuestionDTO submissionQuestionToSubmissionQuestionDto(SubmissionQuestion submissionQuestion) {
        Object questionResponse;
        String response = submissionQuestion.getResponse();
        String[] multiResponse = submissionQuestion.getMultiResponse();
        SubmissionQuestionDTO submissionQuestionDTO = SubmissionQuestionDTO.builder()
                .questionId(submissionQuestion.getQuestionId()).questionTitle(submissionQuestion.getFieldTitle())
                .build();
        if (response == null && multiResponse == null)
            return submissionQuestionDTO;

        switch (submissionQuestion.getResponseType()) {
            case YesNo, Dropdown, ShortAnswer, LongAnswer, Numeric:
                questionResponse = response;
                break;
            case MultipleSelection:
                questionResponse = multiResponse;
                break;
            case AddressInput:
                questionResponse = buildAddress(multiResponse);
                break;
            case Date:
                questionResponse = buildDate(multiResponse);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + submissionQuestion.getResponseType());
        }

        submissionQuestionDTO.setQuestionResponse(questionResponse);
        return submissionQuestionDTO;
    }

    private LocalDate buildDate(String[] multiResponse) {
        return LocalDate.of(parseInt(multiResponse[2]), parseInt(multiResponse[1]), parseInt(multiResponse[0]));
    }

    private AddressDTO buildAddress(String[] multiResponse) {
        return AddressDTO.builder().addressLine1(multiResponse[0]).addressLine2(multiResponse[1]).town(multiResponse[2])
                .county(multiResponse[3]).postcode(multiResponse[4]).build();
    }

}

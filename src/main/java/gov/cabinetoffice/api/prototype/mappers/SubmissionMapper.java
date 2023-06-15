package gov.cabinetoffice.api.prototype.mappers;

import gov.cabinetoffice.api.prototype.dtos.submission.SubmissionDTO;
import gov.cabinetoffice.api.prototype.dtos.submission.SubmissionQuestionDTO;
import gov.cabinetoffice.api.prototype.dtos.submission.SubmissionSectionDTO;
import gov.cabinetoffice.api.prototype.entities.Submission;
import gov.cabinetoffice.api.prototype.models.submission.SubmissionQuestion;
import gov.cabinetoffice.api.prototype.models.submission.SubmissionSection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SubmissionMapper {

    @Mapping(source = "id", target = "submissionId")
    @Mapping(source = "application.applicationName", target = "applicationFormName")
    // TODO is adminEmail the same as scheme.email? Don't store grantApplicant email
    @Mapping(source = "scheme.email", target = "grantAdminEmailAddress")
    @Mapping(source = "scheme.email", target = "grantApplicantEmailAddress")
    @Mapping(source = "scheme.ggisIdentifier", target = "ggisReferenceNumber")
    @Mapping(source = "submittedDate", target = "submittedTimeStamp")
    @Mapping(source = "definition.sections",target = "sections", qualifiedByName = "mapSections")
    SubmissionDTO submissionToSubmissionDto(Submission submission);

     @Named("mapSections")
     default List<SubmissionSectionDTO> mapSections(List<SubmissionSection> sections) {
     return submissionSectionListToSubmissionSectionDtoList(sections);
     }

     @Mapping(source = "sectionId", target = "sectionId")
     @Mapping(source = "sectionTitle", target = "sectionTitle")
     @Mapping(target = "questions", qualifiedByName = "mapQuestions")
     SubmissionSectionDTO submissionSectionToSubmissionSectionDto(SubmissionSection
     submissionSection);

     default List<SubmissionSectionDTO> submissionSectionListToSubmissionSectionDtoList(
     List<SubmissionSection> submissionSections) {
     List<SubmissionSectionDTO> submissionSectionDTOs = new ArrayList<>();
     for (SubmissionSection section : submissionSections) {
     submissionSectionDTOs.add(submissionSectionToSubmissionSectionDto(section));
     }
     return submissionSectionDTOs;
     }

     @Mapping(source = "questionId", target = "questionId")
     @Mapping(source = "fieldTitle", target = "questionTitle")
     @Mapping(source = "response", target = "questionResponse") // TODO deal with
     // multi-response
     SubmissionQuestionDTO submissionQuestionToSubmissionQuestionDto(SubmissionQuestion
     submissionQuestion);

     @Named("mapQuestions")
     default List<SubmissionQuestionDTO>
     submissionQuestionListToSubmissionQuestionDtoList(
     List<SubmissionQuestion> submissionQuestions) {
     List<SubmissionQuestionDTO> submissionQuestionDTOList = new ArrayList<>();
     for (SubmissionQuestion submissionQuestion : submissionQuestions) {
     submissionQuestionDTOList.add(submissionQuestionToSubmissionQuestionDto(submissionQuestion));
     }
     return submissionQuestionDTOList;
     }

}

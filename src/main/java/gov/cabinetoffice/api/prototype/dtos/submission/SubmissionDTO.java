package gov.cabinetoffice.api.prototype.dtos.submission;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gov.cabinetoffice.api.prototype.enums.SubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmissionDTO {

    private ApplicationDTO application;

    private LocalDateTime created;

    private ZonedDateTime submittedDate;

    private SubmissionStatus status;

    private String gapId;

    private SubmissionDefinitionDTO formQuestionsAndAnswers;

    private SchemeDTO scheme;

    private GrantApplicantOrganisationProfileDTO createdBy;

}

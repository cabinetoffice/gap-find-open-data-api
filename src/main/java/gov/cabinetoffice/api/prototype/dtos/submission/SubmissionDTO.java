package gov.cabinetoffice.api.prototype.dtos.submission;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmissionDTO {

    private String submissionId;

    private String applicationFormName;

    private String grantAdminEmailAddress;

    private String grantApplicantEmailAddress;

    private String ggisReferenceNumber;

    private ZonedDateTime submittedTimeStamp;

    private List<SubmissionSectionDTO> sections;

}

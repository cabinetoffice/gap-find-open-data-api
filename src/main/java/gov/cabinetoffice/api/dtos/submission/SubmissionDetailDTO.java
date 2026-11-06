package gov.cabinetoffice.api.dtos.submission;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmissionDetailDTO {

	private UUID submissionId;

	private String gapId;

	private String ggisReferenceNumber;

	private String applicationFormName;

	private String grantApplicantEmailAddress;

	private ZonedDateTime submittedTimeStamp;

	private List<SubmissionSectionDTO> sections;

}

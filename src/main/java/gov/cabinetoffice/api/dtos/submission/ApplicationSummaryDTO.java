package gov.cabinetoffice.api.dtos.submission;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationSummaryDTO {

    private String applicationFormName;

    private String grantAdminEmailAddress;

    private String ggisReferenceNumber;

    private int applicationFormVersion;

    private int totalSubmissions;

    private int totalSubmissionPages;
}

package gov.cabinetoffice.api.prototype.dtos.submission;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gov.cabinetoffice.api.prototype.entities.Submission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmissionsDTO {

    private List<SubmissionDTO> submissions;

}

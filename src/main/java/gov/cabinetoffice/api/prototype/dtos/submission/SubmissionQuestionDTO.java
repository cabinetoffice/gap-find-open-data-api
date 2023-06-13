package gov.cabinetoffice.api.prototype.dtos.submission;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gov.cabinetoffice.api.prototype.enums.ResponseTypeEnum;
import gov.cabinetoffice.api.prototype.models.submission.SubmissionQuestionValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmissionQuestionDTO {

    private String fieldTitle;

    private String response;

    private String[] multiResponse;

}

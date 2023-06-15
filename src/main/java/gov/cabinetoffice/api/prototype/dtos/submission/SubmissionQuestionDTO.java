package gov.cabinetoffice.api.prototype.dtos.submission;

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
public class SubmissionQuestionDTO {

    private String questionId;

    private String questionTitle; // fieldTitle in table

    private Object questionResponse;

}

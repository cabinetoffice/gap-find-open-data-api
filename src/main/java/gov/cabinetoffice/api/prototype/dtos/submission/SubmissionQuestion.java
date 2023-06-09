package gov.cabinetoffice.api.prototype.dtos.submission;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gov.cabinetoffice.api.prototype.enums.ResponseTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmissionQuestion {

    private String questionId;

    private String profileField;

    private String fieldTitle;

    private String displayText;

    private String hintText;

    private String questionSuffix;

    private String fieldPrefix;

    private String adminSummary;

    private ResponseTypeEnum responseType;

    private SubmissionQuestionValidation validation;

    private String[] options;

    private String response;

    private String[] multiResponse;

    public SubmissionQuestion(SubmissionQuestion other) {
        this(other.questionId, other.profileField, other.fieldTitle, other.displayText, other.hintText,
                other.questionSuffix, other.fieldPrefix, other.adminSummary, other.responseType, other.validation,
                other.options, other.response, other.multiResponse);
    }

}

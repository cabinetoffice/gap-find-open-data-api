package gov.cabinetoffice.api.prototype.dtos.submission;

import gov.cabinetoffice.api.prototype.enums.SubmissionSectionStatus;
import gov.cabinetoffice.api.prototype.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionSectionDTO {

    private String sectionTitle;

    private SubmissionSectionStatus sectionStatus;

    private List<SubmissionQuestionDTO> questions;

}

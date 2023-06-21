package gov.cabinetoffice.api.prototype.models.submission;

import gov.cabinetoffice.api.prototype.enums.SubmissionSectionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionSection {

	private String sectionId;

	private String sectionTitle;

	private SubmissionSectionStatus sectionStatus;

	private List<SubmissionQuestion> questions;

}

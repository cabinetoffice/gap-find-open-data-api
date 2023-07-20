package gov.cabinetoffice.api.models.submission;

import gov.cabinetoffice.api.enums.SubmissionSectionStatus;
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

package gov.cabinetoffice.api.models.submission;

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

	private String sectionStatus;

	private List<SubmissionQuestion> questions;

}

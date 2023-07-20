package gov.cabinetoffice.api.dtos.submission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionSectionDTO {

	private String sectionId;

	private String sectionTitle;

	private List<SubmissionQuestionDTO> questions;

}

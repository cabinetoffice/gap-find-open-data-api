package gov.cabinetoffice.api.dtos.submission;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class SubmissionSectionDTO {

	private String sectionId;

	private String sectionTitle;

	private List<SubmissionQuestionDTO> questions;

}

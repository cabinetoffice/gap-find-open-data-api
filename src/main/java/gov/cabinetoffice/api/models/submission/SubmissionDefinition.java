package gov.cabinetoffice.api.models.submission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionDefinition {

	@Builder.Default
	private List<SubmissionSection> sections = new ArrayList<>();

}

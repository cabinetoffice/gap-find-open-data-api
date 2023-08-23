package gov.cabinetoffice.api.models.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ApplicationFormSection {

	private String sectionId;

	private String sectionTitle;

	private String sectionStatus;

	private List<ApplicationFormQuestion> questions;

}

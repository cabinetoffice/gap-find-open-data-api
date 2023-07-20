package gov.cabinetoffice.api.models.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import gov.cabinetoffice.api.enums.SectionStatusEnum;
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

	private SectionStatusEnum sectionStatus;

	private List<ApplicationFormQuestion> questions;

}

package gov.cabinetoffice.api.prototype.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import gov.cabinetoffice.api.prototype.enums.ApplicationStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(toBuilder = true)
public class ApplicationFormDTO {

    private Integer grantApplicationId;

    private Integer grantSchemeId;

    private String applicationName;

    private ApplicationAuditDTO audit;

    private ApplicationStatusEnum applicationStatus;

    private List<ApplicationFormSectionDTO> sections;

}

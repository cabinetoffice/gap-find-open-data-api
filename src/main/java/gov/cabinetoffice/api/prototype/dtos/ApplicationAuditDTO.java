package gov.cabinetoffice.api.prototype.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ApplicationAuditDTO {

    private Integer version;

    private Instant created;

    private Integer createdBy;

    private Instant lastUpdated;

    private Integer lastUpdateBy;

    private Instant lastPublished;

}
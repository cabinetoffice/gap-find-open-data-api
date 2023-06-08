package gov.cabinetoffice.api.prototype.dtos;

import gov.cabinetoffice.api.prototype.models.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenericErrorDTO {

    private ErrorMessage error;

    public GenericErrorDTO(String errorMessage) {
        this.error = new ErrorMessage(errorMessage);
    }

}

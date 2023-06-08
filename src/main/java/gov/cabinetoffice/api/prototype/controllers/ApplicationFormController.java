package gov.cabinetoffice.api.prototype.controllers;

import gov.cabinetoffice.api.prototype.dtos.GenericErrorDTO;
import gov.cabinetoffice.api.prototype.entities.ApplicationFormEntity;
import gov.cabinetoffice.api.prototype.exceptions.ApplicationFormException;
import gov.cabinetoffice.api.prototype.services.ApplicationFormService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Application Forms", description = "API for handling organisations.")
@RestController
@RequiredArgsConstructor
public class ApplicationFormController {

    private final ApplicationFormService applicationFormService;

    @GetMapping("/{applicationId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application summary retrieved successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApplicationFormEntity.class))),
            @ApiResponse(responseCode = "404", description = "Application not found with given id",
                    content = @Content(mediaType = "application/json")) })
    public ResponseEntity getApplicationById(@PathVariable @NotNull Integer applicationId) {
        try {
            ApplicationFormEntity response = this.applicationFormService.getApplicationById(applicationId);
            return new ResponseEntity(response, HttpStatus.OK);
        }
        catch (ApplicationFormException e) {
            GenericErrorDTO genericErrorDTO = new GenericErrorDTO(e.getMessage());
            return new ResponseEntity(genericErrorDTO, HttpStatus.NOT_FOUND);
        }

    }

}
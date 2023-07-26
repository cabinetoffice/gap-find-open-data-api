package gov.cabinetoffice.api.controllers;

import gov.cabinetoffice.api.entities.ApplicationFormEntity;
import gov.cabinetoffice.api.models.ErrorMessage;
import gov.cabinetoffice.api.services.ApplicationFormService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Application Forms", description = "API for retrieving applications forms data.")
@RestController
@RequestMapping("/application-forms")
@RequiredArgsConstructor
public class ApplicationFormController {

	private final ApplicationFormService applicationFormService;

	@GetMapping("/{applicationId}")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Application summary retrieved successfully.",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ApplicationFormEntity.class))),
			@ApiResponse(responseCode = "404", description = "Application not found with given id",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorMessage.class))) })
	public ResponseEntity<ApplicationFormEntity> getApplicationById(@PathVariable @NotNull int applicationId) {
		final ApplicationFormEntity response = this.applicationFormService.getApplicationById(applicationId);
		return ResponseEntity.ok().body(response);
	}

}
package gov.cabinetoffice.api.prototype.controllers;

import gov.cabinetoffice.api.prototype.config.S3ConfigProperties;
import gov.cabinetoffice.api.prototype.dtos.GenericErrorDTO;
import gov.cabinetoffice.api.prototype.entities.ApplicationFormEntity;
import gov.cabinetoffice.api.prototype.services.ApplicationFormService;
import gov.cabinetoffice.api.prototype.services.S3Service;
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

	private final S3Service s3Service;

	private final S3ConfigProperties s3ConfigProperties;

	@GetMapping("/{applicationId}")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Application summary retrieved successfully.",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ApplicationFormEntity.class))),
			@ApiResponse(responseCode = "404", description = "Application not found with given id",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = GenericErrorDTO.class))) })
	public ResponseEntity<ApplicationFormEntity> getApplicationById(@PathVariable @NotNull int applicationId) {
		final ApplicationFormEntity response = this.applicationFormService.getApplicationById(applicationId);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/buckets")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Application summary retrieved successfully.",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ApplicationFormEntity.class))),
			@ApiResponse(responseCode = "404", description = "Application not found with given id",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = GenericErrorDTO.class))) })
	public ResponseEntity<String> getBuckets() {
		final String response = this.s3Service.createPresignedURL(s3ConfigProperties.getSourceBucket(),
				"1/81ccea53-9d35-4acf-8cdb-883dfe22e9e9/273acbe3-c937-496e-86f8-f5a0166843c3/2022-07-08 Grant Submission Responses - Definition - Grant applicant Programme - Confluence.pdf");
		return ResponseEntity.ok().body(response);
	}

}

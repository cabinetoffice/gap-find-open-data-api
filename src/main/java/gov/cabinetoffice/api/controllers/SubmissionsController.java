package gov.cabinetoffice.api.controllers;

import gov.cabinetoffice.api.dtos.submission.SubmissionListDTO;
import gov.cabinetoffice.api.entities.Submission;
import gov.cabinetoffice.api.models.ErrorMessage;
import gov.cabinetoffice.api.services.SubmissionsService;
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

@RestController
@RequestMapping("/submissions")
@Tag(name = "Submissions", description = "API for handling applicants submissions")
@RequiredArgsConstructor
public class SubmissionsController {

	private final SubmissionsService submissionsService;

	@GetMapping("/{applicationId}")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Submissions retrieved successfully.",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = Submission.class))),
			@ApiResponse(responseCode = "404", description = "No submissions found with given application id",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorMessage.class))) })
	public ResponseEntity<SubmissionListDTO> getSubmissionByApplicationId(@PathVariable @NotNull int applicationId) {
		final SubmissionListDTO response = this.submissionsService.getSubmissionByApplicationId(applicationId);
		return ResponseEntity.ok().body(response);
	}

}

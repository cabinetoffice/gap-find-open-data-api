package gov.cabinetoffice.api.controllers;

import gov.cabinetoffice.api.dtos.submission.ApplicationListDTO;
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
	private static final int FUNDING_ORG_ID = 1; // TODO hard coding until we can take this value from spring security

	private final SubmissionsService submissionsService;

	@GetMapping
		@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", description = "Submissions retrieved successfully.",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = Submission.class)
					)
			),
			@ApiResponse(
					responseCode = "404", description = "No submissions found for funding organisation",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorMessage.class)
					)
			)
		})
	public ResponseEntity<ApplicationListDTO> getSubmissions() {
		final ApplicationListDTO response = this.submissionsService.getSubmissionsByFundingOrgId(FUNDING_ORG_ID);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/{ggisReferenceNumber}")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", description = "Submissions retrieved successfully.",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = Submission.class)
					)
			),
			@ApiResponse(
					responseCode = "404", description = "No submissions found for funding organisation or GGIS ID",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorMessage.class)
					)
			)
	})
	public ResponseEntity<ApplicationListDTO> getSubmissionsByGgisRefNum(@PathVariable @NotNull String ggisReferenceNumber) {
		final ApplicationListDTO response = this.submissionsService.getSubmissionsByFundingOrgIdAndGgisReferenceNum(FUNDING_ORG_ID, ggisReferenceNumber);
		return ResponseEntity.ok().body(response);
	}
}

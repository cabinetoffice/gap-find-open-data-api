package gov.cabinetoffice.api.controllers;

import gov.cabinetoffice.api.dtos.submission.ApplicationListDTO;
import gov.cabinetoffice.api.dtos.submission.CountResponseDTO;
import gov.cabinetoffice.api.models.ErrorMessage;
import gov.cabinetoffice.api.services.SubmissionsService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/submissions")
@Tag(name = "Submissions", description = "API for handling applicants submissions")
@RequiredArgsConstructor
public class SubmissionsController {

	private final SubmissionsService submissionsService;

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Submissions retrieved successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApplicationListDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "Invalid API key provided",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "No submissions found for funding organisation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            )
    })
    public ResponseEntity<ApplicationListDTO> getSubmissions(final Principal principal, @RequestParam(name = "page", required = false) Integer page) {
        final int fundingOrganisationId = Integer.parseInt(principal.getName());
        log.info("funding organisation: " + fundingOrganisationId);
        log.info("requested page: " + page);

        final ApplicationListDTO response;
        if (page == null || page < 1) {
            response = this.submissionsService.getSubmissionsByFundingOrgId(fundingOrganisationId);
        } else {
            response = this.submissionsService.getSubmissionsByFundingOrgId(fundingOrganisationId, page);
        }
        log.debug("results of submissionsService.getSubmissionsByFundingOrgId");
        log.debug(response.toString());

        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<ApplicationListDTO> getSubmissions(final Principal principal) {
        return getSubmissions(principal, null);
    }

    @GetMapping("/count")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Count retrieved successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CountResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "Invalid API key provided",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            )
    })
    public ResponseEntity<CountResponseDTO> getSubmissionsCount(final Principal principal) {
        final int fundingOrganisationId = Integer.parseInt(principal.getName());
        log.info("funding organisation: " + fundingOrganisationId);
        final CountResponseDTO response = this.submissionsService.getSubmissionsCountByFundingOrgId(fundingOrganisationId);
        return ResponseEntity.ok().body(response);
    }

	@GetMapping("/{ggisReferenceNumber}")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", description = "Submissions retrieved successfully.",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ApplicationListDTO.class)
					)
			),
			@ApiResponse(
					responseCode = "403", description = "Invalid API key provided",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorMessage.class)
					)
			),
			@ApiResponse(
					responseCode = "404", description = "No submissions found for funding organisation or GGIS ID",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorMessage.class)
					)
			)
	})
	public ResponseEntity<ApplicationListDTO> getSubmissionsByGgisRefNum(@PathVariable @NotNull final String ggisReferenceNumber, final Principal principal, @RequestParam(name = "page", required = false) Integer page) {
		final int fundingOrganisationId = Integer.parseInt(principal.getName());
		log.info("funding organisation: " + fundingOrganisationId + ", GGIS ID: " + ggisReferenceNumber);
		log.info("requested page: " + page);

        final ApplicationListDTO response;
        if (page == null || page < 1) {
            response = this.submissionsService.getSubmissionsByFundingOrgIdAndGgisReferenceNum(fundingOrganisationId, ggisReferenceNumber);
        } else {
            response = this.submissionsService.getSubmissionsByFundingOrgIdAndGgisReferenceNum(fundingOrganisationId, ggisReferenceNumber, page);
        }
		log.debug("results of submissionsService.getSubmissionsByFundingOrgIdAndGgisReferenceNum");
		log.debug(response.toString());

		return ResponseEntity.ok().body(response);
	}

	public ResponseEntity<ApplicationListDTO> getSubmissionsByGgisRefNum(@PathVariable @NotNull final String ggisReferenceNumber, final Principal principal) {
		return getSubmissionsByGgisRefNum(ggisReferenceNumber, principal, null);
	}

	@GetMapping("/{ggisReferenceNumber}/count")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", description = "Count retrieved successfully.",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = CountResponseDTO.class)
					)
			),
			@ApiResponse(
					responseCode = "403", description = "Invalid API key provided",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorMessage.class)
					)
			)
	})
	public ResponseEntity<CountResponseDTO> getSubmissionsCountByGgis(@PathVariable @NotNull final String ggisReferenceNumber, final Principal principal) {
		final int fundingOrganisationId = Integer.parseInt(principal.getName());
		log.info("funding organisation: " + fundingOrganisationId + ", GGIS ID: " + ggisReferenceNumber);
		final CountResponseDTO response = this.submissionsService.getSubmissionsCountByFundingOrgIdAndGgisReferenceNum(fundingOrganisationId, ggisReferenceNumber);
		return ResponseEntity.ok().body(response);
	}
}

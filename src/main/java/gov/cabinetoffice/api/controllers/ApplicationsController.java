package gov.cabinetoffice.api.controllers;

import gov.cabinetoffice.api.dtos.submission.ApplicationSummaryListDTO;
import gov.cabinetoffice.api.models.ErrorMessage;
import gov.cabinetoffice.api.services.SubmissionsService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/applications")
@Tag(name = "Applications", description = "API for application summaries (no submissions)")
@RequiredArgsConstructor
public class ApplicationsController {

    private final SubmissionsService submissionsService;

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Applications retrieved successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApplicationSummaryListDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "Invalid API key provided",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "No applications found for funding organisation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            )
    })
    public ResponseEntity<ApplicationSummaryListDTO> getApplications(
            final Principal principal,
            @RequestParam(name = "page", required = false) Integer page) {

        final int fundingOrganisationId = Integer.parseInt(principal.getName());
        log.info("funding organisation: {}", fundingOrganisationId);
        log.info("requested page: {}", page);

        final ApplicationSummaryListDTO response = (page == null || page < 1)
                ? submissionsService.getApplicationSummariesByFundingOrgId(fundingOrganisationId)
                : submissionsService.getApplicationSummariesByFundingOrgId(fundingOrganisationId, page);

        return ResponseEntity.ok(response);
    }
}

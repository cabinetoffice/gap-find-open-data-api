package gov.cabinetoffice.api.prototype.controllers;

import gov.cabinetoffice.api.prototype.dtos.GenericErrorDTO;
import gov.cabinetoffice.api.prototype.entities.Submission;
import gov.cabinetoffice.api.prototype.services.SubmissionsService;
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

import java.util.List;

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
                            schema = @Schema(implementation = GenericErrorDTO.class))) })
    public ResponseEntity getSubmissionByApplicationId(@PathVariable @NotNull Integer applicationId) {
        final List<Submission> response = this.submissionsService.getSubmissionByApplicationId(applicationId);
        return ResponseEntity.ok().body(response);
    }

}

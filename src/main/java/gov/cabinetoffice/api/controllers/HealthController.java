package gov.cabinetoffice.api.controllers;

import gov.cabinetoffice.api.entities.Submission;
import gov.cabinetoffice.api.models.ErrorMessage;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Tag(name = "Health check", description = "API for checking app status")
@RequiredArgsConstructor
public class HealthController {

    @GetMapping()
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application is running.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Submission.class))),
            @ApiResponse(responseCode = "404", description = "Application is not running",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)))})
    public ResponseEntity<String> getHealth() {
        return ResponseEntity.ok().body("Application is running");
    }

}

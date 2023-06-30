package gov.cabinetoffice.api.prototype.controllers;

import gov.cabinetoffice.api.prototype.dtos.CreateApiKeyDTO;
import gov.cabinetoffice.api.prototype.models.ErrorMessage;
import gov.cabinetoffice.api.prototype.services.ApiGatewayService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apiGateway")
@Tag(name = "Api Gateway", description = "API for handling api gateway")
@RequiredArgsConstructor
public class ApiGatewayController {

	private final ApiGatewayService apiGatewayService;

	@PostMapping("/create-key")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Api Key created successfully.",
					content = @Content(mediaType = "text/plain",
							examples = @ExampleObject(value = "Key created successfully"))),
			@ApiResponse(responseCode = "400", description = "Bad request",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorMessage.class))) })
	public ResponseEntity<String> createKey(@RequestBody CreateApiKeyDTO createApiKeyDTO) {
		apiGatewayService.createApiKeys(createApiKeyDTO.getKeyName(), createApiKeyDTO.getKeyDescription());
		return ResponseEntity.ok().body("Key created successfully");
	}

	@DeleteMapping("/delete-key/{keyName}")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Api Key deleted successfully.",
					content = @Content(mediaType = "text/plain",
							examples = @ExampleObject(value = "Key deleted successfully"))),

			@ApiResponse(responseCode = "400", description = "Bad request",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorMessage.class))) })
	public ResponseEntity<String> deleteKey(@PathVariable @NotNull String keyName) {
		apiGatewayService.deleteApiKeys(keyName);
		return ResponseEntity.ok().body("Key deleted successfully");
	}

}

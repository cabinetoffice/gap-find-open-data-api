package gov.cabinetoffice.api.prototype.controllers;

import gov.cabinetoffice.api.prototype.dtos.CreateApiKeyDTO;
import gov.cabinetoffice.api.prototype.services.ApiGatewayService;
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
	public ResponseEntity createKey(@RequestBody CreateApiKeyDTO createApiKeyDTO) {
		apiGatewayService.createApiKeys(createApiKeyDTO.getKeyName(), createApiKeyDTO.getKeyDescription());
		return ResponseEntity.ok().body("Key created successfully");
	}

	@DeleteMapping("/delete-key/{keyName}")
	public ResponseEntity deleteKey(@PathVariable @NotNull String keyName) {
		apiGatewayService.deleteApiKeys(keyName);
		return ResponseEntity.ok().body("Key deleted successfully");
	}

}

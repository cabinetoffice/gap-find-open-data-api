package gov.cabinetoffice.api.prototype.services;

import gov.cabinetoffice.api.prototype.config.ApiGatewayConfigProperties;
import gov.cabinetoffice.api.prototype.exceptions.ApiKeyAlreadyExistException;
import gov.cabinetoffice.api.prototype.exceptions.ApiKeyDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.apigateway.ApiGatewayClient;
import software.amazon.awssdk.services.apigateway.model.CreateApiKeyRequest;
import software.amazon.awssdk.services.apigateway.model.CreateApiKeyResponse;
import software.amazon.awssdk.services.apigateway.model.CreateUsagePlanKeyRequest;
import software.amazon.awssdk.services.apigateway.model.GetApiKeysResponse;

@Service
@RequiredArgsConstructor
public class ApiGatewayService {

	private final ApiGatewayConfigProperties apiGatewayConfigProperties;

	private final ApiGatewayClient apiGatewayClient;

	private static GetApiKeysResponse getAllApiKeys(ApiGatewayClient client) {
		return client.getApiKeys();
	}

	public void createApiKeys(String keyName, String keyDescription) {
		checkIfKeyExistAlready(keyName);

		CreateApiKeyRequest apiKeyRequest = CreateApiKeyRequest.builder()
			.name(keyName)
			.description(keyDescription)
			.enabled(true)
			.generateDistinctId(true)
			.build();

		// Creating a api key
		CreateApiKeyResponse response = apiGatewayClient.createApiKey(apiKeyRequest);

		// set the usage plan for the created api key.
		CreateUsagePlanKeyRequest planRequest = CreateUsagePlanKeyRequest.builder()
			.usagePlanId(apiGatewayConfigProperties.getApiGatewayUsagePlanId())
			.keyId(response.id())
			.keyType("API_KEY")
			.build();

		apiGatewayClient.createUsagePlanKey(planRequest);

	}

	public void deleteApiKeys(String keyName) {
		getAllApiKeys(apiGatewayClient).items()
			.stream()
			.filter(k -> k.name() != null && k.name().equals(keyName))
			.findFirst()
			.ifPresentOrElse(k -> {
				apiGatewayClient.deleteApiKey(builder -> builder.apiKey(k.id()));
			}, () -> {
				throw new ApiKeyDoesNotExistException("API Key with name " + keyName + " does not exist");
			});
	}

	private void checkIfKeyExistAlready(String keyName) {
		getAllApiKeys(apiGatewayClient).items()
			.stream()
			.filter(key -> key.name().equals(keyName))
			.findFirst()
			.ifPresent(key -> {
				throw new ApiKeyAlreadyExistException("API Key with name " + keyName + " already exists");
			});
	}

}

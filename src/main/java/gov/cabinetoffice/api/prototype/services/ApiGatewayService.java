package gov.cabinetoffice.api.prototype.services;

import gov.cabinetoffice.api.prototype.config.ApiGatewayConfigProperties;
import gov.cabinetoffice.api.prototype.config.AwsClientConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.apigateway.ApiGatewayClient;
import software.amazon.awssdk.services.apigateway.model.ApiGatewayException;
import software.amazon.awssdk.services.apigateway.model.ApiKey;
import software.amazon.awssdk.services.apigateway.model.CreateApiKeyRequest;
import software.amazon.awssdk.services.apigateway.model.CreateApiKeyResponse;
import software.amazon.awssdk.services.apigateway.model.CreateUsagePlanKeyRequest;
import software.amazon.awssdk.services.apigateway.model.GetApiKeysResponse;

@Service
@RequiredArgsConstructor
public class ApiGatewayService {

	@Autowired
	ApiGatewayConfigProperties apiGatewayConfigProperties;

	@Autowired
	AwsClientConfig awsClientConfig;

	public void createApiKeys(String keyName, String keyDescription) {
		ApiGatewayClient client = createClient();
		// TODO check it's not already existing
		if (doesApiKeyExists(client, keyName)) {
			throw new RuntimeException("API Key already exists");
		}

		try {
			CreateApiKeyRequest apiKeyRequest = CreateApiKeyRequest.builder()
				.name(keyName)
				.description(keyDescription)
				.enabled(true)
				.generateDistinctId(true)
				.build();

			// Creating a api key
			CreateApiKeyResponse response = client.createApiKey(apiKeyRequest);

			// set the usage plan for the created api key.
			CreateUsagePlanKeyRequest planRequest = CreateUsagePlanKeyRequest.builder()
				.usagePlanId(apiGatewayConfigProperties.getApiGatewayUsagePlanId())
				.keyId(response.id())
				.keyType("API_KEY")
				.build();

			client.createUsagePlanKey(planRequest);
			client.close();

		}
		catch (ApiGatewayException e) {
			// TODO throw appropriate exception
			System.err.println(e.awsErrorDetails().errorMessage());
			System.exit(1);
		}
	}

	public void deleteApiKeys(String keyName) {
		ApiGatewayClient client = createClient();
		try {
			ApiKey key = getAllApiKeys(client).items()
				.stream()
				.filter(k -> k.name() != null && k.name().equals(keyName))
				.findFirst()
				.orElse(null);
			if (key == null) {
				// TODO throw appropriate exception
				throw new RuntimeException("API Key does not exist");
			}
			client.deleteApiKey(builder -> builder.apiKey(key.id()));
			client.close();

		}
		catch (ApiGatewayException e) {

			System.err.println(e.awsErrorDetails().errorMessage());
			System.exit(1);
		}
	}

	private static GetApiKeysResponse getAllApiKeys(ApiGatewayClient client) {
		return client.getApiKeys();
	}

	private ApiGatewayClient createClient() {
		return ApiGatewayClient.builder()
			.region(Region.of(awsClientConfig.getRegion()))
			.credentialsProvider(
					() -> AwsBasicCredentials.create(awsClientConfig.getAccessKeyId(), awsClientConfig.getSecretKey()))
			.build();
	}

	private boolean doesApiKeyExists(ApiGatewayClient client, String keyName) {
		return getAllApiKeys(client).items().stream().anyMatch(key -> key.name() != null && key.name().equals(keyName));
	}

}

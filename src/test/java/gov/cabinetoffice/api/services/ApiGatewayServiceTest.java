package gov.cabinetoffice.api.services;

import gov.cabinetoffice.api.config.ApiGatewayConfigProperties;
import gov.cabinetoffice.api.exceptions.ApiKeyAlreadyExistException;
import gov.cabinetoffice.api.exceptions.ApiKeyDoesNotExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.apigateway.ApiGatewayClient;
import software.amazon.awssdk.services.apigateway.model.ApiKey;
import software.amazon.awssdk.services.apigateway.model.CreateApiKeyRequest;
import software.amazon.awssdk.services.apigateway.model.CreateApiKeyResponse;
import software.amazon.awssdk.services.apigateway.model.CreateUsagePlanKeyRequest;
import software.amazon.awssdk.services.apigateway.model.CreateUsagePlanKeyResponse;
import software.amazon.awssdk.services.apigateway.model.GetApiKeysResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiGatewayServiceTest {

    private final String API_KEY_NAME = "apikeyName";
    private final String API_KEY_DESCRIPTION = "apikeyDescription";
    private final ApiKey apiKey = ApiKey.builder().name(API_KEY_NAME).build();
    private final GetApiKeysResponse getApiKeysResponse = GetApiKeysResponse.builder().items(List.of(apiKey)).build();
    @Mock
    ApiGatewayConfigProperties apiGatewayConfigProperties;
    @Mock
    ApiGatewayClient apiGatewayClient;
    @InjectMocks
    ApiGatewayService apiGatewayService;

    @Test
    void createApiKeys() {
        when(apiGatewayClient.getApiKeys()).thenReturn(GetApiKeysResponse.builder().build());

        CreateApiKeyResponse apiKeyRequest = CreateApiKeyResponse.builder().description(API_KEY_DESCRIPTION).name(API_KEY_NAME).build();
        when(apiGatewayClient.createApiKey(any(CreateApiKeyRequest.class))).thenReturn(apiKeyRequest);

        CreateUsagePlanKeyResponse usagePlanKeyResponse = CreateUsagePlanKeyResponse.builder().build();
        when(apiGatewayClient.createUsagePlanKey(any(CreateUsagePlanKeyRequest.class))).thenReturn(usagePlanKeyResponse);

        apiGatewayService.createApiKeys(API_KEY_NAME, API_KEY_DESCRIPTION);

        verify(apiGatewayClient).createApiKey(any(CreateApiKeyRequest.class));
        verify(apiGatewayClient).createUsagePlanKey(any(CreateUsagePlanKeyRequest.class));
    }

    @Test
    void deleteApiKeys() {
        when(apiGatewayClient.getApiKeys()).thenReturn(getApiKeysResponse);
        assertDoesNotThrow(() -> apiGatewayService.deleteApiKeys(API_KEY_NAME));
    }

    @Test
    void deleteApiKeys_throwsApiKeyDoesNotExistException() {
        when(apiGatewayClient.getApiKeys()).thenReturn(getApiKeysResponse);
        assertThrows(ApiKeyDoesNotExistException.class, () -> apiGatewayService.deleteApiKeys("anotherKeyName"));
    }


    @Test
    void checkIfKeyExistAlready_throwsApiKeyAlreadyExistException() {
        when(apiGatewayClient.getApiKeys()).thenReturn(getApiKeysResponse);
        assertThrows(ApiKeyAlreadyExistException.class, () -> apiGatewayService.checkIfKeyExistAlready(API_KEY_NAME));
    }

    @Test
    void checkIfKeyExistAlready_doesNotThrowException() {
        when(apiGatewayClient.getApiKeys()).thenReturn(getApiKeysResponse);
        assertDoesNotThrow(() -> apiGatewayService.checkIfKeyExistAlready("anotherKeyName"));
    }

}
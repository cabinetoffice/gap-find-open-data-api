package gov.cabinetoffice.api.client;

import gov.cabinetoffice.api.config.UserServiceConfig;
import gov.cabinetoffice.api.dtos.user.UserDto;
import gov.cabinetoffice.api.exceptions.InvalidBodyException;
import gov.cabinetoffice.api.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceClientTest {

    private final String sub = "d522c5ac-dea1-4d79-ba07-62d5c7203da1";
    private final String domain = "domain";
    private final String url = domain + "/user?userSub={userSub}";
    private final String publicKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA9tFWBEI5fBdN5pmnUcZJ6xziLC0JcsXPyRIs9SnR7iQ4UEn8zQf+uZuJkTkUFQ9Yk2dHJv/jiFo/sxDWmNIDT6SqQXhCbvkwzo3F8Q8fto6BRr8fPMTyETfO9GQwzTJoGmlZ6/BOBNw34/AdD5UVyu8/o62aVwWsVhI98Ivp/WH2BNFApfb6OB2/5kG1cJkdbNq3mJnyUkPfUemomcSMem3jXmW42olGk1O8ytsVOVRt53LU1vY8yTkf2QUAMrI2cnpEHu4EVVmuTtLCQIzDwJUZc2A4xXGyPMIZwEc2/5/2rR1l5NfFEMCfABbGQ+e2UOMXLG/4/mUz7Qcln/EpFwIDAQAB";
    @Mock
    private UserServiceConfig userServiceConfig;
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private UserServiceClient userServiceClient;

    @BeforeEach
    void setup(){
        when(userServiceConfig.getDomain()).thenReturn(domain);
        when(userServiceConfig.getSecret()).thenReturn("secret");
        when(userServiceConfig.getPublicKey()).thenReturn(publicKeyString);
    }

    @Test
    void getUserForSub_Successful() {

        final UserDto expectedUserDto = UserDto.builder().emailAddress("email@email.com").build();

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(UserDto.class), anyMap()))
                .thenReturn(new ResponseEntity<>(expectedUserDto, HttpStatus.OK));

        final UserDto result = userServiceClient.getUserForSub(sub);

        assertThat(result.getEmailAddress()).isEqualTo(expectedUserDto.getEmailAddress());
    }

    @Test
    void getUserForSub_EmptyBody() {

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), any(Class.class),
                any(Map.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        assertThrows(InvalidBodyException.class, () -> userServiceClient.getUserForSub(sub),
                "Null body from " + url + "where sub is : " + sub + " in user service");
    }

    @Test
    void getUserForSub_404() {
        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), any(Class.class),
                any(Map.class))).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(UserNotFoundException.class, () -> userServiceClient.getUserForSub(sub),
                "User not found for sub " + sub + " in user service");
    }

    @Test
    void getUserForSub_Exception() {

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), any(Class.class),
                any(Map.class))).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(HttpServerErrorException.class, () -> userServiceClient.getUserForSub(sub));
    }

    @Test
    void getUserForSub_UnexpectedError() {

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(UserDto.class), anyMap()))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(HttpClientErrorException.class, () -> userServiceClient.getUserForSub(sub));
    }

    @Test
    void getUserForSub_EncryptionError() {
        when(userServiceConfig.getPublicKey()).thenReturn("wrongFormatKey");

        assertThrows(RuntimeException.class,()-> userServiceClient.getUserForSub(sub));
    }
}
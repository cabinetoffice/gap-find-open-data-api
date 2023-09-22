package gov.cabinetoffice.api.client;

import gov.cabinetoffice.api.config.UserServiceConfig;
import gov.cabinetoffice.api.dtos.user.UserDto;
import gov.cabinetoffice.api.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static gov.cabinetoffice.api.mappers.SubmissionMapperTestData.GRANT_APPLICANT_EMAIL_ADDRESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceClientTest {

    @Mock
    private UserServiceConfig userServiceConfig;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserServiceClient userServiceClient;

    @Test
    public void getUserForSub_Successful() {
        final String sub = "d522c5ac-dea1-4d79-ba07-62d5c7203da1";
        final String domain = "domain";
        final String url = domain + "/user?userSub={userSub}";
        final UserDto expectedUserDto = UserDto.builder().emailAddress(GRANT_APPLICANT_EMAIL_ADDRESS).build();

        when(userServiceConfig.getDomain()).thenReturn(domain);
        when(userServiceConfig.getLambdaSecret()).thenReturn("secret");
        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(UserDto.class), anyMap()))
                .thenReturn(new ResponseEntity(expectedUserDto, HttpStatus.OK));

        final UserDto result = userServiceClient.getUserForSub(sub);

        assertThat(result.getEmailAddress()).isEqualTo(expectedUserDto.getEmailAddress());
    }


    @Test
    public void getUserForSub_EmptyBody() {
        final String sub = "d522c5ac-dea1-4d79-ba07-62d5c7203da1";
        final String domain = "domain";
        final String expectedUrl = domain + "/user?userSub={userSub}";

        when(userServiceConfig.getDomain()).thenReturn(domain);
        when(userServiceConfig.getLambdaSecret()).thenReturn("secret");
        when(restTemplate.exchange(eq(expectedUrl), eq(HttpMethod.GET), any(HttpEntity.class), any(Class.class), any(Map.class)))
                .thenReturn(new ResponseEntity(HttpStatus.OK));

        assertThrows(
                UserNotFoundException.class,
                () -> userServiceClient.getUserForSub(sub),
                "User not found for sub " + sub + " in user service"
        );
    }

    @Test
    public void getUserForSub_404() {
        final String sub = "d522c5ac-dea1-4d79-ba07-62d5c7203da1";
        final String domain = "domain";
        final String expectedUrl = domain + "/user?userSub={userSub}";

        when(userServiceConfig.getDomain()).thenReturn(domain);
        when(userServiceConfig.getLambdaSecret()).thenReturn("secret");
        when(restTemplate.exchange(eq(expectedUrl), eq(HttpMethod.GET), any(HttpEntity.class), any(Class.class), any(Map.class)))
                .thenReturn(new ResponseEntity(HttpStatus.NOT_FOUND));

        assertThrows(
                UserNotFoundException.class,
                () -> userServiceClient.getUserForSub(sub),
                "User not found for sub " + sub + " in user service"
        );
    }

    @Test
    public void getUserForSub_Exception() {
        final String sub = "d522c5ac-dea1-4d79-ba07-62d5c7203da1";
        final String domain = "domain";
        final String expectedUrl = domain + "/user?userSub={userSub}";

        when(userServiceConfig.getDomain()).thenReturn(domain);
        when(userServiceConfig.getLambdaSecret()).thenReturn("secret");
        when(restTemplate.exchange(eq(expectedUrl), eq(HttpMethod.GET), any(HttpEntity.class), any(Class.class), any(Map.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(
                HttpServerErrorException.class,
                () -> userServiceClient.getUserForSub(sub)
        );
    }
}

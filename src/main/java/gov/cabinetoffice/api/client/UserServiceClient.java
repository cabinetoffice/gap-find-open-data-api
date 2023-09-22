package gov.cabinetoffice.api.client;

import gov.cabinetoffice.api.config.UserServiceConfig;
import gov.cabinetoffice.api.dtos.user.UserDto;
import gov.cabinetoffice.api.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceClient {
    private final UserServiceConfig userServiceConfig;
    private final RestTemplate restTemplate;

    public UserDto getUserForSub(String sub) {
        final String url = userServiceConfig.getDomain() + "/user?userSub={userSub}";
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", userServiceConfig.getLambdaSecret());
        final HttpEntity<String> requestEntity = new HttpEntity<>(null, requestHeaders);
        final Map<String, String> params = Collections.singletonMap("userSub", sub);

        final ResponseEntity<UserDto> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, UserDto.class, params);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new UserNotFoundException("User not found for sub " + sub + " in user service");
        }
    }
}

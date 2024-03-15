package gov.cabinetoffice.api.client;

import gov.cabinetoffice.api.config.UserServiceConfig;
import gov.cabinetoffice.api.dtos.user.UserDto;
import gov.cabinetoffice.api.exceptions.InvalidBodyException;
import gov.cabinetoffice.api.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
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
        requestHeaders.add("Authorization", encryptSecret(userServiceConfig.getSecret(), userServiceConfig.getPublicKey()));
        final HttpEntity<String> requestEntity = new HttpEntity<>(null, requestHeaders);
        final Map<String, String> params = Collections.singletonMap("userSub", sub);
        ResponseEntity<UserDto> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, UserDto.class, params);
            if (response.getBody() != null) {
                return response.getBody();
            } else {
                throw new InvalidBodyException("Null body from " + url + "where sub is : " + sub + " in user service");
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError()) {
                throw new UserNotFoundException("User not found for sub " + sub + " in user service");
            } else {
                throw e;
            }
        }

    }

    public  String encryptSecret(String secret, String publicKey) {

        try {
            final byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);
            final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            final PublicKey rsaPublicKey = keyFactory.generatePublic(keySpec);
            final Cipher encryptCipher = Cipher.getInstance("RSA");

            encryptCipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
            final byte[] cipherText = encryptCipher.doFinal(secret.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while encrypting the secret " + e);
        }
    }
}

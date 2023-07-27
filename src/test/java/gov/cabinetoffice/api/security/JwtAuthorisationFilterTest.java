package gov.cabinetoffice.api.security;

import com.auth0.jwt.exceptions.MissingClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import gov.cabinetoffice.api.config.JwtProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class JwtAuthorisationFilterTest {

    private JwtAuthorisationFilter jwtAuthorisationFilter;

    @Mock
    private JwtProperties jwtProperties;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private final String secret = "qwertyuiopasdfghjklzxcvbnm123456";

    @BeforeEach
    void setup() {
        jwtAuthorisationFilter = new JwtAuthorisationFilter(jwtProperties);
    }

    @Test
    void doFilterInternal_validToken() throws ServletException, IOException {
        String validToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE2MjczMTcyMjksImV4cCI6bnVsbCwiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoianJvY2tldEBleGFtcGxlLmNvbSIsImZ1bmRlcl9pZCI6IjEifQ.sAJYOAHiUMNsRC0m-nHkd0PbAnf_hXnejC7TOrhmJ9g";
        String funderId = "1";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtProperties.getSecretKey()).thenReturn(secret);

        jwtAuthorisationFilter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        verify(filterChain).doFilter(request, response);
        assertThat(authentication).isNotNull();
        assertThat(authentication.isAuthenticated()).isTrue();
        assertThat(authentication.getPrincipal()).isEqualTo(funderId);
    }

    @Test
    public void doFilterInternal_expiredToken() {
        String expiredToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE2MjczMTcyMjksImV4cCI6MTY1ODc2NjgyOSwiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoianJvY2tldEBleGFtcGxlLmNvbSJ9._GRx1ijhhNtc9-octUluGFZV4KMk840Do6v5JuypVNg";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + expiredToken);
        when(jwtProperties.getSecretKey()).thenReturn(secret);

        assertThatExceptionOfType(TokenExpiredException.class)
                .isThrownBy(() -> jwtAuthorisationFilter.doFilterInternal(request, response, filterChain))
                .withMessage("The Token has expired on 2022-07-25T16:33:49Z.");
    }
    @Test
    public void doFilterInternal_invalidSignature() {
        String invalidSignatureToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE2OTA0NzIyNzEsImV4cCI6bnVsbCwiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoianJvY2tldEBleGFtcGxlLmNvbSIsImZ1bmRlcl9pZCI6IjEifQ.mOsIGE5FWjMC6GehQMurPuTnJ8Sk5Rp-DDai3YHCol8";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + invalidSignatureToken);
        when(jwtProperties.getSecretKey()).thenReturn(secret);

        assertThatExceptionOfType(SignatureVerificationException.class)
                .isThrownBy(() -> jwtAuthorisationFilter.doFilterInternal(request, response, filterChain))
                .withMessage("The Token's Signature resulted invalid when verified using the Algorithm: HmacSHA256");
    }

    @Test
    public void doFilterInternal_missingFunderIdClaim() {
        String missingClaimToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE2MjczMTcyMjksImV4cCI6bnVsbCwiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoianJvY2tldEBleGFtcGxlLmNvbSJ9.9s-eCLOLEWCf59KnJdxPvscW7pzzzGB6u4i6sMh_8Fk";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + missingClaimToken);
        when(jwtProperties.getSecretKey()).thenReturn(secret);

        assertThatExceptionOfType(MissingClaimException.class)
                .isThrownBy(() -> jwtAuthorisationFilter.doFilterInternal(request, response, filterChain))
                .withMessage("The Claim 'funder_id' is not present in the JWT.");
    }

}

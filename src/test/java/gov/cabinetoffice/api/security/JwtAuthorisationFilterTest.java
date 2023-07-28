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
        final String validToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJmdW5kZXJfaWQiOiIxIn0.NbYwfVIpqalW1gM204pQXM7o6voNoO7EyFwl1XjXEQQ";
        final String funderId = "1";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtProperties.getSecretKey()).thenReturn(secret);

        jwtAuthorisationFilter.doFilterInternal(request, response, filterChain);

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        verify(filterChain).doFilter(request, response);
        assertThat(authentication).isNotNull();
        assertThat(authentication.isAuthenticated()).isTrue();
        assertThat(authentication.getPrincipal()).isEqualTo(funderId);
    }

    @Test
    public void doFilterInternal_expiredToken() {
        final String expiredToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTg3NjY4Mjl9.q_Xbd-a0o48MDDJeO9O4-Bscc9NeHcOUMpqTM41C3Bw";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + expiredToken);
        when(jwtProperties.getSecretKey()).thenReturn(secret);

        assertThatExceptionOfType(TokenExpiredException.class)
                .isThrownBy(() -> jwtAuthorisationFilter.doFilterInternal(request, response, filterChain))
                .withMessage("The Token has expired on 2022-07-25T16:33:49Z.");
    }
    @Test
    public void doFilterInternal_invalidSignature() {
        final String invalidSignatureToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJmdW5kZXJfaWQiOiIxIn0.j_uP1WWerR1pSMetHh1sNXtbbK5R7uncKgX09maodRY";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + invalidSignatureToken);
        when(jwtProperties.getSecretKey()).thenReturn(secret);

        assertThatExceptionOfType(SignatureVerificationException.class)
                .isThrownBy(() -> jwtAuthorisationFilter.doFilterInternal(request, response, filterChain))
                .withMessage("The Token's Signature resulted invalid when verified using the Algorithm: HmacSHA256");
    }

    @Test
    public void doFilterInternal_missingFunderIdClaim() {
        final String missingClaimToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOm51bGx9.kBir5OUYCklX8bSu9j_74bkeywZmY95ockG7-driY9A";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + missingClaimToken);
        when(jwtProperties.getSecretKey()).thenReturn(secret);

        assertThatExceptionOfType(MissingClaimException.class)
                .isThrownBy(() -> jwtAuthorisationFilter.doFilterInternal(request, response, filterChain))
                .withMessage("The Claim 'funder_id' is not present in the JWT.");
    }

}

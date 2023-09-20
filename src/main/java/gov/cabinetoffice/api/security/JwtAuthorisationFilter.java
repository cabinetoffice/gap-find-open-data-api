package gov.cabinetoffice.api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.MissingClaimException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import gov.cabinetoffice.api.config.JwtProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorisationFilter extends OncePerRequestFilter {

    private final JwtProperties jwtProperties;
    private static final String FUNDER_ID = "funder_id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (!request.getRequestURI().contains("actuator")) {
            final String token = request.getHeader("jwt"); //TODO I think we maybe want to move this into the authorization header
            log.info("token is {} for request {} {}", token, request.getMethod(), request.getRequestURI());

            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            final DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(jwtProperties.getSecretKey())).build().verify(token);

            final Claim funderId = decodedJWT.getClaims().get(FUNDER_ID);
            log.info("funder ID from token: " + funderId);

            if (funderId == null) {
                log.info("funder ID is null"); // if we ever get here in production then something has gone badly wrong somewhere
                throw new MissingClaimException(FUNDER_ID);
            }

            final Authentication authentication = new UsernamePasswordAuthenticationToken(funderId.asString(), null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Created security principal for funding organisation: " + authentication.getName());
        }

        filterChain.doFilter(request, response);
    }
}

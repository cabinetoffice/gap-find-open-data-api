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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthorisationFilter extends OncePerRequestFilter {

    private final JwtProperties jwtProperties;
    private static final String FUNDER_ID = "funder_id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String token = request.getHeader("jwt");
        //TODO remove
        System.out.println("HEADER: " + request.getHeaderNames());
        System.out.println("TOKEN: " + token);
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        final DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(jwtProperties.getSecretKey()))
                .build()
                .verify(token);

        final Claim funderId = decodedJWT.getClaims().get(FUNDER_ID);

        if (funderId == null) {
            throw new MissingClaimException(FUNDER_ID);
        }

        final Authentication authentication = new UsernamePasswordAuthenticationToken(funderId.asString(),null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}

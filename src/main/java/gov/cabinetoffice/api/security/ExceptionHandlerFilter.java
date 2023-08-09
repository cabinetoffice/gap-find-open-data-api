package gov.cabinetoffice.api.security;

import com.auth0.jwt.exceptions.MissingClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException {
        try {
            filterChain.doFilter(request, response);
        }
        catch (TokenExpiredException | SignatureVerificationException | MissingClaimException | ServletException e) {
            log.error("an error occurred", e);

            String message = switch (e.getClass().getSimpleName()) {
                case "TokenExpiredException" -> "JWT is expired";
                case "SignatureVerificationException" -> "JWT is not valid";
                case "MissingClaimException" -> "JWT is missing expected properties";
                default -> "An error occurred";
            };

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(message);
            response.getWriter().flush();
        }
    }
}

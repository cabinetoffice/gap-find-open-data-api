package gov.cabinetoffice.api.security;

import com.auth0.jwt.exceptions.MissingClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        }
        catch (TokenExpiredException | SignatureVerificationException | MissingClaimException e) {
            String message = switch (e.getClass().getSimpleName()) {
                case "TokenExpiredException" -> "JWT is expired";
                case "SignatureVerificationException" -> "JWT is not valid";
                case "MissingClaimException" -> "JWT is missing expected properties";
                default -> "Invalid JWT";
            };

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(message);
            response.getWriter().flush();
        }
        catch (RuntimeException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("JWT is not valid");
            response.getWriter().flush();
        }
    }
}

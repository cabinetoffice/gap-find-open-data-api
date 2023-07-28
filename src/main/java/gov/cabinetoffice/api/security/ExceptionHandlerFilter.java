package gov.cabinetoffice.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException {
        try {
            filterChain.doFilter(request, response);
        }
        catch (Exception e) {
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
    }
}

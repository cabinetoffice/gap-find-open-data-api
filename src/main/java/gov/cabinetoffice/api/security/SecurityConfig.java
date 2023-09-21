package gov.cabinetoffice.api.security;

import gov.cabinetoffice.api.config.JwtProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class SecurityConfig {

    private static final String[] WHITE_LIST = {"/actuator/health","/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**", "/swagger-ui.html",
            "/webjars/**"};
    private final JwtAuthorisationFilter jwtAuthorisationFilter;

    public SecurityConfig(final JwtProperties jwtProperties) {
        this.jwtAuthorisationFilter = new JwtAuthorisationFilter(jwtProperties);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // specify any paths you don't want subject to JWT validation/authentication
        return web -> web.ignoring().requestMatchers(WHITE_LIST);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> request.anyRequest()
                                .authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .addFilterBefore(new ExceptionHandlerFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtAuthorisationFilter, ExceptionHandlerFilter.class);

        return http.build();
    }
}

package gov.cabinetoffice.api.prototype.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "OpenApi Prototype",
		description = "Prototype to give an idea on what is achievable", version = "2.0"))
public class OpenApiConfig {

}

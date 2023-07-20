package gov.cabinetoffice.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "GAP Find Open Data API",
		description = "API for retrieving application form data from the admin database.", version = "4.0"))
public class OpenApiConfig {

}

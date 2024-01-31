package gov.cabinetoffice.api.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;

import java.util.List;

@Configuration
@AllArgsConstructor
@SecurityScheme(name = "api key", type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER, paramName = "x-api-key")
public class OpenApiConfig {

    SwaggerConfig swaggerConfig;

    @Bean
    public OpenAPI openApi() {
        Server server = new Server();
        server.setUrl(swaggerConfig.getApiGatewayUrl());


        Info info = new Info()
                .title("GAP Find Open Data API")
                .version("1.0")
                .description("This API exposes endpoints to grab submissions.");

        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.addList("api key");

        return new OpenAPI().info(info).servers(List.of(server)).security(List.of(securityRequirement));
    }
}



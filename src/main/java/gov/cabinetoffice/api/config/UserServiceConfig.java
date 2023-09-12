package gov.cabinetoffice.api.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Configuration("cognitoConfigurationProperties")
@ConfigurationProperties(prefix = "user-service")
public class UserServiceConfig {

    private String domain;


}

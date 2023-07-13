package gov.cabinetoffice.api.prototype.config;

import jakarta.validation.constraints.NotNull;
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
@Configuration("apiGatewayConfigurationProperties")
@ConfigurationProperties(prefix = "aws")
public class ApiGatewayConfigProperties {

	@NotNull
	private String apiGatewayId;

	@NotNull
	private String apiGatewayUsagePlanId;

}

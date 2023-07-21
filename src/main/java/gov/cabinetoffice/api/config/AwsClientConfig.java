package gov.cabinetoffice.api.config;

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
@Configuration("awsClientConfigurationProperties")
@ConfigurationProperties(prefix = "aws")
public class AwsClientConfig {

	@NotNull
	private String secretKey;

	@NotNull
	private String accessKeyId;

	@NotNull
	private String region;

}

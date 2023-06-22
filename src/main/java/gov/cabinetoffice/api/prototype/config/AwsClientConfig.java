package gov.cabinetoffice.api.prototype.config;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;

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

	@Bean
	public AwsCredentialsProvider awsCredentialsProvider() {
		return StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretKey));
	}

	@Bean
	public Region region() {
		return Region.of(region);
	}

}

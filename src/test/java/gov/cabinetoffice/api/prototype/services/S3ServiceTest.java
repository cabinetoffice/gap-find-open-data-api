package gov.cabinetoffice.api.prototype.services;

import gov.cabinetoffice.api.prototype.config.AwsClientConfig;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
class S3ServiceTest {

	@Mock
	AwsClientConfig awsClientConfig;

	@Mock
	private S3Presigner s3Presigner;

	@InjectMocks
	private S3Service s3Service;

	private final String BUCKET_NAME = "test-bucket-name";

	final String REGION = "eu-west-2";

	private final String OBJECT_KEY = "test-object-key";

	@Test
	public void testCreatePresignedURL() {
		final String EXPECTED_URL_START = "https://" + BUCKET_NAME + ".s3." + REGION + ".amazonaws.com/" + OBJECT_KEY
				+ "?";

		final PresignedGetObjectRequest presigned = s3Presigner.presignGetObject(
				getObjectPresignerRequest -> getObjectPresignerRequest.signatureDuration(Duration.ofMinutes(15))
					.getObjectRequest(getObjectRequest -> getObjectRequest.bucket(BUCKET_NAME).key(OBJECT_KEY)));

		when(awsClientConfig.getAccessKeyId()).thenReturn("accessKeyId");
		when(awsClientConfig.getSecretKey()).thenReturn("secretKey");
		when(awsClientConfig.getRegion()).thenReturn(REGION);

		when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class))).thenReturn(presigned);

		final String resultUrl = s3Service.createPresignedURL(BUCKET_NAME, OBJECT_KEY);

		assertThat(resultUrl).startsWith(EXPECTED_URL_START);
	}

}
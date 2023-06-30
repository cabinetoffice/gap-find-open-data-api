package gov.cabinetoffice.api.prototype.services;

import gov.cabinetoffice.api.prototype.config.AwsClientConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3Service {

	// TODO decide on duration
	public static final int URL_DURATION = 15;

	@Autowired
	private AwsClientConfig awsClientConfig;

	public String createPresignedURL(String bucketName, String objectKey) {
		// create client with credentials
		final S3Presigner presigner = S3Presigner.builder()
			.region(Region.of(awsClientConfig.getRegion()))
			.credentialsProvider(
					() -> AwsBasicCredentials.create(awsClientConfig.getAccessKeyId(), awsClientConfig.getSecretKey()))
			.build();

		// Create a GetObjectPresignRequest to specify the signature duration
		final GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
			.signatureDuration(Duration.ofSeconds(URL_DURATION))
			.getObjectRequest(getObjectRequest -> getObjectRequest.bucket(bucketName).key(objectKey))
			.build();

		// Generate the presigned request
		final PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);
		presigner.close();
		return presignedGetObjectRequest.url().toString();
	}

}

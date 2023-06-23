package gov.cabinetoffice.api.prototype.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3Service {

	private final S3Client s3Client;

	public static final int URL_DURATION = 1;

	@Autowired
	public S3Service(AwsCredentialsProvider awsCredentialsProvider, Region region) {
		this.s3Client = S3Client.builder().region(region).credentialsProvider(awsCredentialsProvider).build();
	}

	public String createPresignedURL(String bucketName, String objectKey) {
		final S3Presigner presigner = S3Presigner.create();

		// Create a GetObjectRequest to be pre-signed
		final GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(objectKey).build();

		// Create a GetObjectPresignRequest to specify the signature duration
		final GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
			.signatureDuration(Duration.ofDays(URL_DURATION))// TODO check this is the right duration
			.getObjectRequest(getObjectRequest)
			.build();

		// Generate the presigned request
		final PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);

		// Log the presigned URL, for example.
		System.out.println("Presigned URL: " + presignedGetObjectRequest.url());
		return presignedGetObjectRequest.url().toString();
	}

}

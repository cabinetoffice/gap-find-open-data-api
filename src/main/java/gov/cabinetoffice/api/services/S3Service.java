package gov.cabinetoffice.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3Service {

	// TODO decide on duration
	public static final int URL_DURATION = 15;

	private final S3Presigner presigner;

	public String createPresignedURL(String bucketName, String objectKey) {

		// Create a GetObjectPresignRequest to specify the signature duration
		final GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
			.signatureDuration(Duration.ofSeconds(URL_DURATION))
			.getObjectRequest(getObjectRequest -> getObjectRequest.bucket(bucketName).key(objectKey))
			.build();

		// Generate the presigned request and return the Url
		return presigner.presignGetObject(getObjectPresignRequest).url().toString();
	}

}

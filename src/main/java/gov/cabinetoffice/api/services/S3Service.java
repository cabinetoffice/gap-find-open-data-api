package gov.cabinetoffice.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3Service {

	public static final int URL_DURATION = 15;

	private final S3Presigner presigner;

	public String createPresignedURL(String bucketName, String objectKey) {
		final GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
			.signatureDuration(Duration.ofMinutes(URL_DURATION))
			.getObjectRequest(getObjectRequest -> getObjectRequest.bucket(bucketName).key(objectKey))
			.build();

		return presigner.presignGetObject(getObjectPresignRequest).url().toString();
	}

}

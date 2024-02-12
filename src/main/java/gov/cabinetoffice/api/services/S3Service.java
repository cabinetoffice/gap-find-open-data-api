package gov.cabinetoffice.api.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

	public static final int URL_DURATION = 15;

	private final S3Presigner presigner;

    public String createPresignedURL(String bucketName, String objectKey) {
		log.info("Creating presigned URL for object with key {}", objectKey);

        final GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(URL_DURATION))
                .getObjectRequest(getObjectRequest -> getObjectRequest.bucket(bucketName).key(objectKey))
                .build();

		return presigner.presignGetObject(getObjectPresignRequest).url().toString();
	}

}

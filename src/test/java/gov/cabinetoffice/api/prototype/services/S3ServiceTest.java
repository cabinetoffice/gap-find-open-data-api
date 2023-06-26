package gov.cabinetoffice.api.prototype.services;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
class S3ServiceTest {

	@Mock
	private S3Presigner s3Presigner;

	@InjectMocks
	private S3Service s3Service;

	private final String BUCKET_NAME = "testBucketName";

	private final String OBJECT_KEY = "testObjectKey";

	private final Instant NOW = Instant.now();

	@Test
	public void testCreatePresignedURL() throws MalformedURLException {
		String expectedUrl = "https://example.com/presigned-url";

		try (MockedStatic<S3Presigner> mockedPresigner = mockStatic(S3Presigner.class)) {
			mockedPresigner.when(S3Presigner::create).thenReturn(s3Presigner);

			PresignedGetObjectRequest presignedRequest = mock(PresignedGetObjectRequest.class);

			when(presignedRequest.url()).thenReturn(new URL(expectedUrl));
			when(presignedRequest.expiration()).thenReturn(NOW.plus(Duration.ofDays(S3Service.URL_DURATION)));
			when(presignedRequest.isBrowserExecutable()).thenReturn(true);
			when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class))).thenReturn(presignedRequest);

			String resultUrl = s3Service.createPresignedURL(BUCKET_NAME, OBJECT_KEY);

			verify(s3Presigner)
				.presignGetObject(argThat((GetObjectPresignRequest request) -> request.signatureDuration()
					.equals(Duration.ofDays(S3Service.URL_DURATION))
						&& request.getObjectRequest().bucket().equals(BUCKET_NAME)
						&& request.getObjectRequest().key().equals(OBJECT_KEY)));

			assertEquals(expectedUrl, resultUrl);
		}
	}

}
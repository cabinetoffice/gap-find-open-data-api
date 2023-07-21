package gov.cabinetoffice.api.services;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.net.MalformedURLException;
import java.net.URL;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

	@Mock
	private S3Presigner s3PresignerMock;

	@Mock
	private PresignedGetObjectRequest presignedRequest;

	@InjectMocks
	private S3Service s3Service;

	private final String BUCKET_NAME = "test-bucket-name";

	private final String OBJECT_KEY = "test-object-key";

	@Test
	void testCreatePresignedURL() throws MalformedURLException {
		final URL url = new URL("https://a-pre-signed-url.com");

		when(s3PresignerMock.presignGetObject(Mockito.any(GetObjectPresignRequest.class))).thenReturn(presignedRequest);
		when(presignedRequest.url()).thenReturn(url);

		final String resultUrl = s3Service.createPresignedURL(BUCKET_NAME, OBJECT_KEY);

		verify(s3PresignerMock).presignGetObject(Mockito.any(GetObjectPresignRequest.class));
		assertThat(resultUrl).isEqualTo(url.toString());
	}

}
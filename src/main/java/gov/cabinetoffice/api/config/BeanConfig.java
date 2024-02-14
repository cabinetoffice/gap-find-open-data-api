package gov.cabinetoffice.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@RequiredArgsConstructor
@Configuration
public class BeanConfig {

    private final S3ConfigProperties s3ConfigProperties;

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.of(s3ConfigProperties.getRegion()))
                .build();
    }

}

package gov.cabinetoffice.api.prototype;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class ApplicationRunnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationRunnerApplication.class, args);
    }

}

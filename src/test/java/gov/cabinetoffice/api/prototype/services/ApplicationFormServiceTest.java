package gov.cabinetoffice.api.prototype.services;

import gov.cabinetoffice.api.prototype.entities.ApplicationFormEntity;
import gov.cabinetoffice.api.prototype.exceptions.ApplicationFormException;
import gov.cabinetoffice.api.prototype.repositories.ApplicationFormRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
public class ApplicationFormServiceTest {

    @Mock
    private ApplicationFormRepository applicationFormRepository;

    @InjectMocks
    private ApplicationFormService applicationFormService;

    private final Integer APPLICATION_ID = 1;

    @Test
    void getApplicationById_found() {
        ApplicationFormEntity applicationForm = ApplicationFormEntity.builder().applicationName("test")
                .grantApplicationId(APPLICATION_ID).build();

        when(applicationFormRepository.findById(APPLICATION_ID)).thenReturn(java.util.Optional.of(applicationForm));

        ApplicationFormEntity response = applicationFormService.getApplicationById(APPLICATION_ID);

        verify(applicationFormRepository).findById(APPLICATION_ID);

        assertThat(response).isEqualTo(applicationForm);
        assertThat(response.getGrantApplicationId()).isEqualTo(APPLICATION_ID);
    }

    @Test
    void getApplicationById_notFound() {
        when(applicationFormRepository.findById(APPLICATION_ID)).thenReturn(Optional.empty());
        Throwable exception = assertThrows(ApplicationFormException.class,
                () -> applicationFormService.getApplicationById(APPLICATION_ID));

        verify(applicationFormRepository).findById(APPLICATION_ID);
        assertThat(exception.getMessage()).isEqualTo("No application found with id " + APPLICATION_ID);
    }

}

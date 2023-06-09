package gov.cabinetoffice.api.prototype.services;

import gov.cabinetoffice.api.prototype.entities.ApplicationFormEntity;
import gov.cabinetoffice.api.prototype.entities.Submission;
import gov.cabinetoffice.api.prototype.exceptions.SubmissionNotFoundException;
import gov.cabinetoffice.api.prototype.repositories.SubmissionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
class SubmissionsServiceTest {

    @Mock
    private SubmissionRepository submissionRepository;

    @InjectMocks
    private SubmissionsService submissionsService;

    private final Integer APPLICATION_ID = 1;

    @Test
    void getSubmissionByApplicationId_found() {
        ApplicationFormEntity applicationForm = ApplicationFormEntity.builder().grantApplicationId(APPLICATION_ID)
                .build();
        Submission submission = Submission.builder().application(applicationForm).build();

        when(submissionRepository.findByApplicationGrantApplicationId(APPLICATION_ID)).thenReturn(List.of(submission));

        List<Submission> response = submissionsService.getSubmissionByApplicationId(APPLICATION_ID);

        verify(submissionRepository).findByApplicationGrantApplicationId(APPLICATION_ID);

        assertThat(response).isEqualTo(List.of(submission));
    }

    @Test
    void getSubmissionByApplicationId_notFound() {
        when(submissionRepository.findByApplicationGrantApplicationId(APPLICATION_ID)).thenReturn(List.of());
        Throwable exception = assertThrows(SubmissionNotFoundException.class,
                () -> submissionsService.getSubmissionByApplicationId(APPLICATION_ID));

        verify(submissionRepository).findByApplicationGrantApplicationId(APPLICATION_ID);
        assertThat(exception.getMessage()).isEqualTo("No submissions found with application id " + APPLICATION_ID);
    }

}
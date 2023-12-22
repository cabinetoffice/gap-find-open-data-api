package gov.cabinetoffice.api.repositories;

import gov.cabinetoffice.api.dtos.submission.ApplicationDTO;
import gov.cabinetoffice.api.dtos.submission.ApplicationListDTO;
import gov.cabinetoffice.api.exceptions.SubmissionNotFoundException;
import gov.cabinetoffice.api.rowmappers.ApplicationDTORowMapper;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class SubmissionJDBCRepositoryTest {

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    @InjectMocks
    private SubmissionJDBCRepository repositoryUnderTest;

    public static final int FUNDING_ORG_ID = 965;
    public static final String GGIS_IDENTIFIER = "SCH-2023-08-07";

    @Test
    void getApplicationSubmissionsByFundingOrganisationId_ReturnsExpectedSubmissions() {
        final ApplicationDTO applicationDTO = ApplicationDTO.builder()
                .applicationId(159836)
                .grantAdminEmailAddress("gavin.cook@and.digital")
                .ggisReferenceNumber("SCH-2023-08-07")
                .applicationFormName("Woodland services grant")
                .build();

        final List<ApplicationDTO> applicationDTOList = List.of(applicationDTO);

        final String query = SubmissionJDBCRepository.APPLICATIONS_WITH_SUBMISSIONS_QUERY +
                SubmissionJDBCRepository.AND_FUNDING_ORG_CLAUSE;

        when(jdbcTemplate.query(eq(query), any(SqlParameterSource.class), any(ApplicationDTORowMapper.class)))
                .thenReturn(applicationDTOList);

        final ApplicationListDTO methodResponse = repositoryUnderTest.getApplicationSubmissionsByFundingOrganisationId(FUNDING_ORG_ID);

        assertThat(methodResponse.getNumberOfResults()).isEqualTo(1);
        assertThat(methodResponse.getApplications()).containsOnly(applicationDTO);
    }

    @ParameterizedTest
    @EmptySource
    void getApplicationSubmissionsByFundingOrganisationId_ThrowsSubmissionNotFoundException(List<ApplicationDTO> submissions) {

        final String query = SubmissionJDBCRepository.APPLICATIONS_WITH_SUBMISSIONS_QUERY +
                SubmissionJDBCRepository.AND_FUNDING_ORG_CLAUSE;

        when(jdbcTemplate.query(eq(query), any(SqlParameterSource.class), any(ApplicationDTORowMapper.class)))
                .thenReturn(submissions);

        assertThatExceptionOfType(SubmissionNotFoundException.class)
                .isThrownBy(() -> repositoryUnderTest.getApplicationSubmissionsByFundingOrganisationId(FUNDING_ORG_ID));
    }

    @Test
    void getApplicationSubmissionsByFundingOrganisationIdAndGgisIdentifier_ReturnsExpectedSubmissions() {
        final ApplicationDTO applicationDTO = ApplicationDTO.builder()
                .applicationId(159836)
                .grantAdminEmailAddress("gavin.cook@and.digital")
                .ggisReferenceNumber("SCH-2023-08-07")
                .applicationFormName("Woodland services grant")
                .build();

        final List<ApplicationDTO> applicationDTOList = List.of(applicationDTO);

        final String query = SubmissionJDBCRepository.APPLICATIONS_WITH_SUBMISSIONS_QUERY +
                SubmissionJDBCRepository.AND_FUNDING_ORG_CLAUSE +
                SubmissionJDBCRepository.AND_GGIS_ID_CLAUSE;

        when(jdbcTemplate.query(eq(query), any(SqlParameterSource.class), any(ApplicationDTORowMapper.class)))
                .thenReturn(applicationDTOList);

        final ApplicationListDTO methodResponse = repositoryUnderTest.getApplicationSubmissionsByFundingOrganisationIdAndGgisIdentifier(FUNDING_ORG_ID, GGIS_IDENTIFIER);

        assertThat(methodResponse.getNumberOfResults()).isEqualTo(1);
        assertThat(methodResponse.getApplications()).containsOnly(applicationDTO);
    }

    @ParameterizedTest
    @EmptySource
    void getApplicationSubmissionsByFundingOrganisationIdAndGgisIdentifier_ThrowsSubmissionNotFoundException(List<ApplicationDTO> submissions) {

        final String query = SubmissionJDBCRepository.APPLICATIONS_WITH_SUBMISSIONS_QUERY +
                SubmissionJDBCRepository.AND_FUNDING_ORG_CLAUSE +
                SubmissionJDBCRepository.AND_GGIS_ID_CLAUSE;

        when(jdbcTemplate.query(eq(query), any(SqlParameterSource.class), any(ApplicationDTORowMapper.class)))
                .thenReturn(submissions);

        assertThatExceptionOfType(SubmissionNotFoundException.class)
                .isThrownBy(() -> repositoryUnderTest.getApplicationSubmissionsByFundingOrganisationIdAndGgisIdentifier(FUNDING_ORG_ID, GGIS_IDENTIFIER));
    }
}
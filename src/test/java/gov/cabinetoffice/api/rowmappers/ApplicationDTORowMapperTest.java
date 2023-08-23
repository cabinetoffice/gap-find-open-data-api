package gov.cabinetoffice.api.rowmappers;

import gov.cabinetoffice.api.dtos.submission.ApplicationDTO;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

@ExtendWith(MockitoExtension.class)
class ApplicationDTORowMapperTest {

    @Mock
    private ResultSet resultSet;
    private final int rowNum = 1;

    private ApplicationDTORowMapper rowMapperUnderTest;

    @BeforeEach
    void setup() {
        rowMapperUnderTest = new ApplicationDTORowMapper();
    }

    @Test
    void mapRow_MapsCorrectValues() throws SQLException {
        final String applicationFormName = "Woodland services grant";
        final String ggisReferenceNumber = "SCH-2023-08-07";
        final String contactEmail = "gavin.cook@and.digital";
        final int applicationId = 136547;

        when(resultSet.getString("applicationFormName"))
                .thenReturn(applicationFormName);
        when(resultSet.getString("ggisReferenceNumber"))
                .thenReturn(ggisReferenceNumber);
        when(resultSet.getString("contactEmail"))
                .thenReturn(contactEmail);
        when(resultSet.getInt("applicationId"))
                .thenReturn(applicationId);

        final ApplicationDTO rowMapperResult = rowMapperUnderTest.mapRow(resultSet, rowNum);

        assertThat(rowMapperResult.getApplicationId()).isEqualTo(applicationId);
        assertThat(rowMapperResult.getApplicationFormName()).isEqualTo(applicationFormName);
        assertThat(rowMapperResult.getGgisReferenceNumber()).isEqualTo(ggisReferenceNumber);
        assertThat(rowMapperResult.getGrantAdminEmailAddress()).isEqualTo(contactEmail);
        assertThat(rowMapperResult.getSubmissions()).isEmpty();

    }
}
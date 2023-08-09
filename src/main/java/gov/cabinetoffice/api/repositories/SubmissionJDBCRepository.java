package gov.cabinetoffice.api.repositories;

import gov.cabinetoffice.api.dtos.submission.ApplicationDTO;
import gov.cabinetoffice.api.dtos.submission.ApplicationListDTO;
import gov.cabinetoffice.api.rowmappers.ApplicationDTORowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class SubmissionJDBCRepository {

    public static final String APPLICATIONS_WITH_SUBMISSIONS_QUERY = """
            SELECT
               DISTINCT ga.grant_application_id AS applicationId,
               gs.ggis_identifier AS ggisReferenceNumber,
               ga.application_name AS applicationFormName,
               gs.scheme_contact AS contactEmail
            FROM grant_submission s
            INNER JOIN grant_scheme gs
               ON gs.grant_scheme_id = s.scheme_id
            INNER JOIN grant_application ga
               ON ga.grant_application_id = s.application_id
            WHERE s.status = 'SUBMITTED'
            """;

    public static final String AND_FUNDING_ORG_CLAUSE = "AND gs.funder_id = :fundingOrgId \n";

    public static final String AND_GGIS_ID_CLAUSE = "AND gs.ggis_identifier = :ggisIdentifier \n";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ApplicationListDTO getApplicationSubmissionsByFundingOrganisationId(final int fundingOrgId) {
        final SqlParameterSource applicationParameters = new MapSqlParameterSource()
                .addValue("fundingOrgId", fundingOrgId);

        final String query = new StringBuilder(APPLICATIONS_WITH_SUBMISSIONS_QUERY)
                .append(AND_FUNDING_ORG_CLAUSE)
                .toString();

        final List<ApplicationDTO> applications = jdbcTemplate.query(query, applicationParameters, new ApplicationDTORowMapper());

        return ApplicationListDTO.builder()
                .applications(applications)
                .numberOfResults(applications.size())
                .build();
    }

    public ApplicationListDTO getApplicationSubmissionsByFundingOrganisationIdAndGgisIdentifier(final int fundingOrgId, final String ggisIdentifier) {
        final SqlParameterSource applicationParameters = new MapSqlParameterSource()
                .addValue("fundingOrgId", fundingOrgId)
                .addValue("ggisIdentifier", ggisIdentifier);

        final String query = new StringBuilder(APPLICATIONS_WITH_SUBMISSIONS_QUERY)
                .append(AND_FUNDING_ORG_CLAUSE)
                .append(AND_GGIS_ID_CLAUSE)
                .toString();

        final List<ApplicationDTO> applications = jdbcTemplate.query(query, applicationParameters, new ApplicationDTORowMapper());

        return ApplicationListDTO.builder()
                .applications(applications)
                .numberOfResults(applications.size())
                .build();
    }
}

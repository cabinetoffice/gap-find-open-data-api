package gov.cabinetoffice.api.repositories;

import gov.cabinetoffice.api.dtos.submission.ApplicationDTO;
import gov.cabinetoffice.api.dtos.submission.ApplicationListDTO;
import gov.cabinetoffice.api.exceptions.SubmissionNotFoundException;
import gov.cabinetoffice.api.rowmappers.ApplicationDTORowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class SubmissionJDBCRepository {

    public static final String APPLICATIONS_WITH_SUBMISSIONS_QUERY = """
            SELECT
               DISTINCT ga.grant_application_id AS applicationId,
               gs.ggis_identifier AS ggisReferenceNumber,
               ga.application_name AS applicationFormName,
               gs.scheme_contact AS contactEmail,
               gs.version AS applicationFormVersion
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

    @SuppressWarnings("StringBufferReplaceableByString")
    public ApplicationListDTO getApplicationSubmissionsByFundingOrganisationId(final int fundingOrgId) {
        // default to first page with page size 100 when no page provided
        return getApplicationSubmissionsByFundingOrganisationId(fundingOrgId, 1);
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    public ApplicationListDTO getApplicationSubmissionsByFundingOrganisationId(final int fundingOrgId, final int page) {
        final int pageSize = 100;
        final int safePage = page < 1 ? 1 : page;
        final int offset = (safePage - 1) * pageSize;

        final SqlParameterSource applicationParameters = new MapSqlParameterSource()
                .addValue("fundingOrgId", fundingOrgId)
                .addValue("limit", pageSize)
                .addValue("offset", offset);

        final String query = new StringBuilder(APPLICATIONS_WITH_SUBMISSIONS_QUERY)
                .append(AND_FUNDING_ORG_CLAUSE)
                .append("LIMIT :limit \n")
                .append("OFFSET :offset \n")
                .toString();

        log.info(query);

        final List<ApplicationDTO> applications = jdbcTemplate.query(query, applicationParameters, new ApplicationDTORowMapper());

        if (applications.isEmpty()) {
            final String msg = "No applications found for this funding organisation";
            log.info(msg);
            throw new SubmissionNotFoundException(msg);
        }

        return ApplicationListDTO.builder()
                .applications(applications)
                .numberOfResults(applications.size())
                .build();
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    public ApplicationListDTO getApplicationSubmissionsByFundingOrganisationIdAndGgisIdentifier(final int fundingOrgId, final String ggisIdentifier) {
        // default to first page with page size 100 when no page provided
        return getApplicationSubmissionsByFundingOrganisationIdAndGgisIdentifier(fundingOrgId, ggisIdentifier, 1);
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    public ApplicationListDTO getApplicationSubmissionsByFundingOrganisationIdAndGgisIdentifier(final int fundingOrgId, final String ggisIdentifier, final int page) {
        final int pageSize = 100;
        final int safePage = page < 1 ? 1 : page;
        final int offset = (safePage - 1) * pageSize;

        final SqlParameterSource applicationParameters = new MapSqlParameterSource()
                .addValue("fundingOrgId", fundingOrgId)
                .addValue("ggisIdentifier", ggisIdentifier)
                .addValue("limit", pageSize)
                .addValue("offset", offset);

        final String query = new StringBuilder(APPLICATIONS_WITH_SUBMISSIONS_QUERY)
                .append(AND_FUNDING_ORG_CLAUSE)
                .append(AND_GGIS_ID_CLAUSE)
                .append("LIMIT :limit \n")
                .append("OFFSET :offset \n")
                .toString();

        log.info(query);

        final List<ApplicationDTO> applications = jdbcTemplate.query(query, applicationParameters, new ApplicationDTORowMapper());

        if (applications.isEmpty()) {
            final String msg = "No applications found for this funding organisation with GGIS identifier " + ggisIdentifier;
            log.info(msg);
            throw new SubmissionNotFoundException(msg);
        }

        return ApplicationListDTO.builder()
                .applications(applications)
                .numberOfResults(applications.size())
                .build();
    }

    public int countApplicationsByFundingOrganisationId(final int fundingOrgId) {
        final String countQuery = """
            SELECT COUNT(DISTINCT ga.grant_application_id) AS total
            FROM grant_submission s
            INNER JOIN grant_scheme gs ON gs.grant_scheme_id = s.scheme_id
            INNER JOIN grant_application ga ON ga.grant_application_id = s.application_id
            WHERE s.status = 'SUBMITTED' AND gs.funder_id = :fundingOrgId
        """;
        final SqlParameterSource params = new MapSqlParameterSource().addValue("fundingOrgId", fundingOrgId);
        Integer result = jdbcTemplate.queryForObject(countQuery, params, Integer.class);
        return result == null ? 0 : result;
    }

    public int countApplicationsByFundingOrganisationIdAndGgisIdentifier(final int fundingOrgId, final String ggisIdentifier) {
        final String countQuery = """
            SELECT COUNT(DISTINCT ga.grant_application_id) AS total
            FROM grant_submission s
            INNER JOIN grant_scheme gs ON gs.grant_scheme_id = s.scheme_id
            INNER JOIN grant_application ga ON ga.grant_application_id = s.application_id
            WHERE s.status = 'SUBMITTED' AND gs.funder_id = :fundingOrgId AND gs.ggis_identifier = :ggisIdentifier
        """;
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("fundingOrgId", fundingOrgId)
                .addValue("ggisIdentifier", ggisIdentifier);
        Integer result = jdbcTemplate.queryForObject(countQuery, params, Integer.class);
        return result == null ? 0 : result;
    }
}

package gov.cabinetoffice.api.rowmappers;


import gov.cabinetoffice.api.dtos.submission.ApplicationDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ApplicationDTORowMapper implements RowMapper<ApplicationDTO> {

    @Override
    public ApplicationDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ApplicationDTO.builder()
                .applicationFormName(rs.getString("applicationFormName"))
                .ggisReferenceNumber(rs.getString("ggisReferenceNumber"))
                .grantAdminEmailAddress(rs.getString("contactEmail"))
                .applicationId(rs.getInt("applicationId"))
                .build();
    }
}

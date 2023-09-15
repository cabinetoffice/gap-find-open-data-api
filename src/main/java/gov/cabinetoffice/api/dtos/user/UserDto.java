package gov.cabinetoffice.api.dtos.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String gapUserId;
    private String emailAddress;
}
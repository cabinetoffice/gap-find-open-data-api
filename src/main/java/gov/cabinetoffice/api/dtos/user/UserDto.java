package gov.cabinetoffice.api.dtos.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String gapUserId;
    private String emailAddress;

    @JsonCreator
    public UserDto(@JsonProperty("gapUserId") String gapUserId, @JsonProperty("emailAddress") String emailAddress) {
        this.gapUserId = gapUserId;
        this.emailAddress = emailAddress;
    }
}
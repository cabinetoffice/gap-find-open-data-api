package gov.cabinetoffice.api.services;

import gov.cabinetoffice.api.client.UserServiceClient;
import gov.cabinetoffice.api.dtos.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserServiceClient userServiceClient;

    public String getUserEmailForSub(String sub) {
        UserDto userDto = userServiceClient.getUserForSub(sub);
        return userDto.getEmailAddress();
    }
}

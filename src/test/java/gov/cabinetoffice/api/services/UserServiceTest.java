package gov.cabinetoffice.api.services;

import gov.cabinetoffice.api.client.UserServiceClient;
import gov.cabinetoffice.api.dtos.user.UserDto;
import gov.cabinetoffice.api.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private UserService userService;

    @Test
    public void getUserEmailForSub_Successful() {
        final String sub = "d522c5ac-dea1-4d79-ba07-62d5c7203da1";
        final UserDto expectedUserDto = UserDto.builder().build();
        when(userServiceClient.getUserForSub(sub)).thenReturn(expectedUserDto);

        final String result = userService.getUserEmailForSub(sub);

        assertThat(result).isEqualTo(expectedUserDto.getEmailAddress());
    }

    @Test
    public void getUserEmailForSub_Exception() {
        final String sub = "d522c5ac-dea1-4d79-ba07-62d5c7203da1";
        when(userServiceClient.getUserForSub(sub)).thenThrow(new UserNotFoundException("User not found for sub " + sub + " in user service"));

        assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserEmailForSub(sub),
                "User not found for sub " + sub + " in user service"
        );
    }

}

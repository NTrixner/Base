package eu.trixner.base.server.service;

import eu.trixner.base.dto.UserDto;
import eu.trixner.base.server.exceptions.UserNotAuthorizedException;
import eu.trixner.base.server.model.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationService {
    UserService userService;

    public void validateUserChange(UserDto userDto) {
        if (userDto.getId() == null) {
            throw new InvalidParameterException();
        }
        UserDto currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new UserNotAuthorizedException();
        }
        if (!(currentUser.getRights().contains(Role.ROLE_USER_CAN_CREATE_USER.getAuthority())
          || (currentUser.getRights().contains(Role.ROLE_USER_CAN_SEE_SELF.getAuthority())
          && Objects.equals(currentUser.getId(), userDto.getId())))) {
            throw new UserNotAuthorizedException();
        }
    }

    public void validateUserDelete(String uuid) {
        UserDto user = userService.findUser(UUID.fromString(uuid));
        if (user == null) {
            throw new NullPointerException();
        }
        validateUserChange(user);
    }
}

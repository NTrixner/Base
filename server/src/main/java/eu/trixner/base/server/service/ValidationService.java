package eu.trixner.base.server.service;

import eu.trixner.base.dto.UserDto;
import eu.trixner.base.server.exceptions.UserNotAuthorizedException;
import eu.trixner.base.server.model.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationService {
    private final UserService userService;

    public void validateUserChange(UserDto userDto) {
        if (userDto.getId() == null) {
            throw new InvalidParameterException();
        }
        UserDto currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new UserNotAuthorizedException();
        }
        if (!StringUtils.equals(currentUser.getUsername(), userDto.getUsername())
          && Objects.equals(userDto.getId(), currentUser.getId())) {
            throw new InvalidParameterException("You shall not change your own username!");
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

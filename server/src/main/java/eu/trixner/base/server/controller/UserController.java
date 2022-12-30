package eu.trixner.base.server.controller;

import eu.trixner.base.dto.*;
import eu.trixner.base.server.model.PasswordResetRequest;
import eu.trixner.base.server.model.UserRegistrationRequest;
import eu.trixner.base.server.service.UserService;
import eu.trixner.base.server.service.ValidationService;
import eu.trixner.base.user.UserApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("api")
public class UserController implements UserApi {
    private final UserService userService;
    private final ValidationService validationService;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    @Override
    @Secured("ROLE_USER_CAN_SEE_SELF")
    public ResponseEntity<UserDto> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @Override
    @Secured("ROLE_USER_CAN_GET_USER_BY_ID")
    public ResponseEntity<UserDto> getUserById(String userId) {
        UserDto dto = userService.findUser(UUID.fromString(userId));
        return ResponseEntity.ok(dto);
    }

    @Override
    @Secured("ROLE_USER_CAN_CHANGE_PASSWORD")
    public ResponseEntity<Void> changePassword(@Valid ChangePasswordDto changePasswordDto) {
        userService.changePassword(changePasswordDto.getUserId(), changePasswordDto.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> registerUser(@Valid RegistrationDto registrationDto) {
        UserRegistrationRequest answer = userService.registerUser(registrationDto, getLocale(registrationDto.getLang()));
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentContextPath();
        String uri = builder.build().toUriString();
        log.info("Registered new user, activate under {}/user/registration/confirmRegistration/{}",
          uri,
          answer.getId());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> confirmRegistration(String id) {
        userService.confirmRegistration(UUID.fromString(id));
        HttpHeaders headers = new HttpHeaders();
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        headers.add(HttpHeaders.LOCATION, uri + "/login?confirmationRegistration=true");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<Void> forgotPassword(@Valid ForgotPasswordDto forgotPasswordDto) {
        PasswordResetRequest req = userService.requestPasswordReset(forgotPasswordDto.getUsername(),
          forgotPasswordDto.getEmail(), getLocale(forgotPasswordDto.getLang()));
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        log.info(
          "Password reset request sent, reset password under {}/user/forgotPassword/resetPassword with " +
            "token {}",
          uri,
          req.getId());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> resetPasswordRequest(@Valid PasswordResetDto dto) {
        userService.resetPassword(UUID.fromString(dto.getUuid()), dto.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Boolean> isEmailAvailable(String email, List<String> ignores) {
        return ResponseEntity.ok(userService.isEmailAvailable(email, ignores));
    }

    @Override
    public ResponseEntity<Boolean> isUsernameAvailable(String username, List<String> ignores) {
        return ResponseEntity.ok(userService.isUsernameAvailable(username, ignores));
    }


    @Override
    public ResponseEntity<Void> changeUser(UserDto userDto) {
        validationService.validateUserChange(userDto);
        userService.changeUser(userDto);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteUser(String uuid) {
        validationService.validateUserDelete(uuid);
        userService.deleteUser(uuid);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<UUID> createUser(UserDto userDto) {
        validationService.validateUserCreate(userDto);
        UUID result = userService.createUser(userDto);
        return ResponseEntity.ok(result);
    }

    private Locale getLocale(String language) {
        try {
            Locale l = Locale.forLanguageTag(language);
            if (l != null) {
                return l;
            }
        } catch (NullPointerException e) {
            log.error("Tried to get a null language ", e);
        }
        return Locale.ENGLISH;
    }
}

package eu.trixner.base.server.controller;

import eu.trixner.base.dto.ForgotPasswordDto;
import eu.trixner.base.dto.PasswordResetDto;
import eu.trixner.base.dto.RegistrationDto;
import eu.trixner.base.dto.UserDto;
import eu.trixner.base.server.model.UserRegistrationRequest;
import eu.trixner.base.server.service.UserService;
import eu.trixner.base.user.UserApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Controller
public class UserController implements UserApi {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return null;
    }

    @Override
    public ResponseEntity<UserDto> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @Override
    public ResponseEntity<UserDto> getUserById(String userId) {
        UserDto dto = userService.findUser(UUID.fromString(userId));
        if (dto == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(dto);
        }
    }

    @Override
    public ResponseEntity<Void> registerUser(@Valid RegistrationDto registrationDto) {
        UserRegistrationRequest answer = userService.registerUser(registrationDto);
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentContextPath();
        String uri = builder.build().toUriString();
        log.info("Registered new user, activate under {}/user/registration/confirmRegistration/{}", uri, answer.getToken());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> confirmRegistration(String token) {
        boolean hasBeenRegistered = userService.confirmRegistration(token);
        if (hasBeenRegistered) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> forgotPassword(@Valid ForgotPasswordDto forgotPasswordDto) {
        return null;
    }

    @Override
    public ResponseEntity<Void> resetPasswordRequest(@Valid PasswordResetDto passwordResetDto) {
        return null;
    }
}

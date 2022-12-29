package eu.trixner.base.server.controller;

import eu.trixner.base.dto.ChangePasswordDto;
import eu.trixner.base.dto.ForgotPasswordDto;
import eu.trixner.base.dto.PasswordResetDto;
import eu.trixner.base.dto.RegistrationDto;
import eu.trixner.base.dto.UserDto;
import eu.trixner.base.server.model.PasswordResetRequest;
import eu.trixner.base.server.model.UserRegistrationRequest;
import eu.trixner.base.server.service.UserService;
import eu.trixner.base.user.UserApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("api")
public class UserController implements UserApi
{
    private final UserService userService;

    @Override
    public Optional<NativeWebRequest> getRequest()
    {
        return Optional.empty();
    }

    @Override
    @Secured("ROLE_USER_CAN_SEE_SELF")
    public ResponseEntity<UserDto> getCurrentUser()
    {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @Override
    @Secured("ROLE_USER_CAN_GET_USER_BY_ID")
    public ResponseEntity<UserDto> getUserById(String userId)
    {
        UserDto dto = userService.findUser(UUID.fromString(userId));
        if (dto == null)
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            return ResponseEntity.ok(dto);
        }
    }

    @Override
    @Secured("ROLE_USER_CAN_CHANGE_PASSWORD")
    public ResponseEntity<Void> changePassword(@Valid ChangePasswordDto changePasswordDto)
    {
        try
        {
            userService.changePassword(changePasswordDto.getOldPassword(), changePasswordDto.getNewPassword());
            return ResponseEntity.ok().build();
        }
        catch (IllegalArgumentException ex)
        {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<Void> registerUser(@Valid RegistrationDto registrationDto)
    {
        UserRegistrationRequest answer = userService.registerUser(registrationDto);
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentContextPath();
        String uri = builder.build().toUriString();
        log.info("Registered new user, activate under {}/user/registration/confirmRegistration/{}",
                uri,
                answer.getId());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> confirmRegistration(String id)
    {
        boolean hasBeenRegistered = userService.confirmRegistration(UUID.fromString(id));
        if (hasBeenRegistered)
        {
            HttpHeaders headers = new HttpHeaders();
            String uri = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            headers.add(HttpHeaders.LOCATION, uri + "/login?confirmationRegistration=true");
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> forgotPassword(@Valid ForgotPasswordDto forgotPasswordDto)
    {
        PasswordResetRequest req = userService.requestPasswordReset(forgotPasswordDto.getUsername(),
                forgotPasswordDto.getEmail());
        if (req == null)
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            String uri = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            log.info(
                    "Password reset request sent, reset password under {}/user/forgotPassword/resetPassword with " +
                            "token {}",
                    uri,
                    req.getId());
            return ResponseEntity.ok().build();
        }
    }

    @Override
    public ResponseEntity<Void> resetPasswordRequest(@Valid PasswordResetDto dto)
    {
        try
        {
            userService.resetPassword(UUID.fromString(dto.getUuid()), dto.getNewPassword());
            return ResponseEntity.ok().build();
        }
        catch (NullPointerException ex)
        {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Boolean> isEmailAvailable(String email)
    {

        return ResponseEntity.ok(userService.isEmailAvailable(email));
    }

    @Override
    public ResponseEntity<Boolean> isUsernameAvailable(String username)
    {
        return ResponseEntity.ok(userService.isUsernameAvailable(username));
    }
}

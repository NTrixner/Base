package eu.trixner.base.server.controller;

import eu.trixner.base.dto.*;
import eu.trixner.base.server.service.UserService;
import eu.trixner.base.server.utils.PaginationUtils;
import eu.trixner.base.user.UserApi;
import eu.trixner.base.user.UserlistApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Controller
public class UserController implements UserApi, UserlistApi {

    UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return null;
    }

    @Override
    public ResponseEntity<Void> confirmRegistration(String registrationQuery) {
        return null;
    }

    @Override
    public ResponseEntity<Void> forgotPassword(@Valid ForgotPasswordDto forgotPasswordDto) {
        return null;
    }

    @Override
    public ResponseEntity<UserDto> getCurrentUser() {
        UserDto userDto = new UserDto();
        userDto.setEmail("asdf@test.com");
        userDto.setUsername("Your name");
        userDto.setId(UUID.randomUUID());

        return ResponseEntity.ok(userDto);
    }

    @Override
    public ResponseEntity<UserDto> getUserById(String userId) {
        UserDto dto = userService.findUser(UUID.fromString(userId));
        if(dto == null){
            return (ResponseEntity<UserDto>) ResponseEntity.notFound();
        }
        else{
            return ResponseEntity.ok(dto);
        }
    }


    @Override
    public ResponseEntity<UserListDto> listUsers(@Valid PaginationRequestDto body) {
        if(body == null){
            return ResponseEntity.ok(userService.getAllUsers());
        }
        else{
            return ResponseEntity.ok(userService.getUsers(PaginationUtils.getPageRequest(body)));
        }


    }

    @Override
    public ResponseEntity<Void> loginUser(@Valid LoginDto loginDto) {
        return null;
    }

    @Override
    public ResponseEntity<Void> logoutUser() {
        return null;
    }

    @Override
    public ResponseEntity<Void> registerUser(@Valid RegistrationDto registrationDto) {
        return null;
    }

    @Override
    public ResponseEntity<Void> resetPasswordRequest(@Valid PasswordResetDto passwordResetDto) {
        return null;
    }
}

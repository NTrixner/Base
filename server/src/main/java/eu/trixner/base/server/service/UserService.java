package eu.trixner.base.server.service;

import eu.trixner.base.dto.RegistrationDto;
import eu.trixner.base.dto.UserDto;
import eu.trixner.base.server.model.PasswordResetRequest;
import eu.trixner.base.server.model.User;
import eu.trixner.base.server.model.UserRegistrationRequest;
import eu.trixner.base.server.repository.PasswordResetRequestRepository;
import eu.trixner.base.server.repository.UserRegistrationRequestRepository;
import eu.trixner.base.server.repository.UserRepository;
import eu.trixner.base.server.service.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRegistrationRequestRepository userRegistrationRequestRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetRequestRepository passwordResetRequestRepository;
    private final EmailService emailService;

    @Value("${user.registration.requestExpiration}")
    private int registrationExpiryDuration;

    @Value("${user.passwordReset.requestExpiration}")
    private int passwordResetExpiryDuration;

    @Override
    public UserDetails loadUserByUsername(String s) {
        User u = userRepository.findByUsernameIgnoreCase(s);
        if (u == null) {
            throw new UsernameNotFoundException("User " + s + " was not found");
        }
        return u;
    }

    private static List<String> cleanStringList(List<String> ignores) {
        return ignores.stream().map(ignore -> ignore.trim().toUpperCase()).collect(Collectors.toList());
    }

    public UserDto getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username);
        UserDto userDto = userMapper.userToUserDto(user);
        userDto.setRights(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        return userDto;
    }

    @Transactional
    public User createUser(RegistrationDto dto) {
        User newUser = userMapper.userRegistrationDtoToUser(dto);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser = this.userRepository.save(newUser);
        return newUser;
    }

    @Transactional
    public UUID createUser(UserDto dto) {
        User newUser = userMapper.userDtoToUser(dto);
        newUser.setPassword(RandomStringUtils.randomAlphanumeric(12));
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser = this.userRepository.save(newUser);
        return newUser.getId();
    }

    @Transactional
    public UserRegistrationRequest registerUser(RegistrationDto registrationDto) {
        User newUser = createUser(registrationDto);

        UserRegistrationRequest newRequest = new UserRegistrationRequest();
        newRequest.setUser(newUser);

        newRequest.setExpiresAt(new Date(System.currentTimeMillis() + registrationExpiryDuration));

        newRequest = this.userRegistrationRequestRepository.save(newRequest);

        emailService.sendUserRegistrationMessage(newUser.getUsername(), newRequest.getId().toString(), newUser.getEmail());
        return newRequest;
    }

    public UserDto findUser(UUID uuid) {
        return userRepository.findById(uuid).map(userMapper::userToUserDto).orElseThrow(NullPointerException::new);
    }

    @Transactional
    public void confirmRegistration(UUID id) {
        UserRegistrationRequest request = this.userRegistrationRequestRepository.findById(id).orElseThrow(NullPointerException::new);
        User user = request.getUser();
        if (request.getExpiresAt().before(new Date())) {
            this.userRegistrationRequestRepository.delete(request);
            this.userRepository.delete(user);
            throw new NullPointerException();
        }
        user.setIsActivated(true);
        this.userRepository.save(user);
        this.userRegistrationRequestRepository.delete(request);
    }

    @Transactional
    public void resetPassword(UUID id, String newPassword) {
        PasswordResetRequest request = passwordResetRequestRepository.findById(id).orElseThrow(NullPointerException::new);
        User user = request.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        int deleted = passwordResetRequestRepository.deleteByUserId(user.getId());
        log.debug("{} Password reset requests were deleted", deleted);
    }

    @Transactional
    public PasswordResetRequest requestPasswordReset(String username, String email) {
        User user = userRepository.findByUsernameIgnoreCaseAndEmailIgnoreCase(username, email);
        if (user == null) {
            throw new NullPointerException();
        }
        PasswordResetRequest request = new PasswordResetRequest();
        request.setUser(user);
        request.setExpiresAt(new Date(System.currentTimeMillis() + passwordResetExpiryDuration));
        request.setMailSent(false);

        request = passwordResetRequestRepository.save(request);
        emailService.sendUserPasswordResetMessage(user.getUsername(), request.getId().toString(), user.getEmail());
        return request;
    }

    /**
     * Scheduled Tasks
     */

    @Scheduled(fixedRateString = "${user.registration.requestCleanupRate}")
    @Transactional
    public void cleanUpRegistrationRequests() {
        List<UserRegistrationRequest> toDelete = userRegistrationRequestRepository.findByExpiresAtIsBefore(new Date());
        int deleted = toDelete.size();
        userRepository.deleteAll(toDelete.stream().map(UserRegistrationRequest::getUser).collect(Collectors.toList()));
        userRegistrationRequestRepository.deleteAll(toDelete);
        log.debug("{} User Registration requests were deleted", deleted);
    }

    @Scheduled(fixedRateString = "${user.passwordReset.requestCleanupRate}")
    @Transactional
    public void cleanUpPasswordResetRequests() {
        int deleted = passwordResetRequestRepository.deleteByExpiresAtIsBefore(new Date());
        log.debug("{} Password reset requests were deleted", deleted);
    }

    @Transactional
    public void changePassword(UUID uid, String newPassword) {
        User user = userRepository.findById(uid).orElseThrow(NullPointerException::new);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.saveAndFlush(user);
    }

    public Boolean isUsernameAvailable(String username, List<String> ignores) {
        if (ignores == null || ignores.size() == 0) {
            ignores = List.of("");
        }
        username = username.trim();
        ignores = cleanStringList(ignores);

        return BooleanUtils.isFalse(userRepository.usernameExists(username, ignores));
    }

    public Boolean isEmailAvailable(String email, List<String> ignores) {
        if (ignores == null || ignores.size() == 0) {
            ignores = List.of("");
        }
        email = email.trim();
        ignores = cleanStringList(ignores);
        return BooleanUtils.isFalse(userRepository.emailExists(email, ignores));
    }

    public void changeUser(UserDto userDto) {
        User byId = userRepository.findById(userDto.getId()).orElseThrow(NullPointerException::new);
        userMapper.updateUser(byId, userDto);
        userRepository.save(byId);
    }

    public void deleteUser(String uuid) {
        userRepository.deleteById(UUID.fromString(uuid));
    }
}


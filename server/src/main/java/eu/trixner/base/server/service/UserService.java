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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRegistrationRequestRepository userRegistrationRequestRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final PasswordResetRequestRepository passwordResetRequestRepository;
    private final EmailService emailService;

    @Value("${user.registration.requestExpiration}")
    private int registrationExpiryDuration;

    @Value("${user.passwordReset.requestExpiration}")
    private int passwordResetExpiryDuration;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       UserRegistrationRequestRepository userRegistrationRequestRepository,
                       PasswordResetRequestRepository passwordResetRequestRepository,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userRegistrationRequestRepository = userRegistrationRequestRepository;
        this.passwordResetRequestRepository = passwordResetRequestRepository;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) {
        User u = userRepository.findByUsernameIgnoreCase(s);
        if (u == null) {
            throw new UsernameNotFoundException("User " + s + " was not found");
        }
        return u;
    }

    public UserDto findUser(UUID uuid) {
        return userRepository.findById(uuid).map(userMapper::userToUserDto).orElse(null);
    }

    public UserDto getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username);
        return userMapper.userToUserDto(user);
    }

    @Transactional
    public User createUser(RegistrationDto dto) {
        User newUser = userMapper.userRegistrationDtoToUser(dto);
        newUser.setPassword(passwordEncoder().encode(newUser.getPassword()));
        newUser = this.userRepository.save(newUser);
        return newUser;
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

    @Transactional
    public boolean confirmRegistration(UUID id) {
        UserRegistrationRequest request = this.userRegistrationRequestRepository.findById(id).orElseThrow(NullPointerException::new);
        User user = request.getUser();
        if (request.getExpiresAt().before(new Date())) {
            this.userRegistrationRequestRepository.delete(request);
            this.userRepository.delete(user);
            return false;
        }
        user.setIsActivated(true);
        this.userRepository.save(user);
        this.userRegistrationRequestRepository.delete(request);
        return true;
    }

    @Transactional
    public PasswordResetRequest requestPasswordReset(String username, String email) {
        User user = userRepository.findByUsernameIgnoreCaseAndEmailIgnoreCase(username, email);
        if (user == null) {
            return null;
        }
        PasswordResetRequest request = new PasswordResetRequest();
        request.setUser(user);
        request.setExpiresAt(new Date(System.currentTimeMillis() + passwordResetExpiryDuration));
        request.setMailSent(false);

        request = passwordResetRequestRepository.save(request);
        emailService.sendUserPasswordResetMessage(user.getUsername(), request.getId().toString(), user.getEmail());
        return request;
    }

    @Transactional
    public void resetPassword(UUID id, String newPassword) {
        PasswordResetRequest request = passwordResetRequestRepository.findById(id).orElseThrow(NullPointerException::new);
        User user = request.getUser();
        user.setPassword(passwordEncoder().encode(newPassword));
        userRepository.save(user);
        int deleted = passwordResetRequestRepository.deleteByUserId(user.getId());
        log.debug("{} Password reset requests were deleted", deleted);
    }

    @Transactional
    public void changePassword(String oldPassword, String newPassword) {
        UserDto currentUser = getCurrentUser();
        User currentUserDatabase = userRepository.findById(currentUser.getId()).orElse(null);
        if (currentUserDatabase == null) {
            throw new NullPointerException();
        }
        if (!passwordEncoder().matches(oldPassword, currentUserDatabase.getPassword())) {
            throw new IllegalArgumentException();
        }
        currentUserDatabase.setPassword(passwordEncoder().encode(newPassword));
        userRepository.saveAndFlush(currentUserDatabase);
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

    /**
     * Getters
     */
    public PasswordEncoder passwordEncoder() {
        return passwordEncoder;
    }

    public Boolean isUsernameAvailable(String username) {
        return BooleanUtils.isFalse(userRepository.existsByUsernameIgnoreCase(username));
    }

    public Boolean isEmailAvailable(String email) {
        return BooleanUtils.isFalse(userRepository.existsByEmailIgnoreCase(email));
    }
}


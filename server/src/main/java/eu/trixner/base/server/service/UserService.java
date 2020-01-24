package eu.trixner.base.server.service;

import eu.trixner.base.dto.RegistrationDto;
import eu.trixner.base.dto.UserDto;
import eu.trixner.base.server.model.User;
import eu.trixner.base.server.model.UserRegistrationRequest;
import eu.trixner.base.server.repository.UserRegistrationRequestRepository;
import eu.trixner.base.server.repository.UserRepository;
import eu.trixner.base.server.service.mapper.UserMapper;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;
    private UserRegistrationRequestRepository userRegistrationRequestRepository;
    private UserMapper userMapper;

    @Value("${user.registration.requestExpiration}")
    private int registrationExpiryDuration;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       UserRegistrationRequestRepository userRegistrationRequestRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userRegistrationRequestRepository = userRegistrationRequestRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) {
        User u = userRepository.findByUsernameIgnoreCase(s);
        if (u == null)
            throw new UsernameNotFoundException("User " + s + " was not found");
        return u;
    }

    public UserDto findUser(UUID uuid) {
        return userRepository.findById(uuid).map(userMapper::userToUserDto).orElse(null);
    }

    public UserDto getCurrentUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userMapper.userToUserDto(user);
    }

    public User createUser(RegistrationDto dto) {
        User newUser = userMapper.userRegistrationDtoToUser(dto);
        newUser.setPassword(passwordEncoder().encode(newUser.getPassword()));
        newUser = this.userRepository.save(newUser);
        return newUser;
    }

    public UserRegistrationRequest registerUser(RegistrationDto registrationDto) {
        User newUser = createUser(registrationDto);

        UserRegistrationRequest newRequest = new UserRegistrationRequest();
        newRequest.setUser(newUser);

        boolean tokenExists = true;
        String token = "";
        while (tokenExists) {
            RandomStringGenerator randomStringGenerator =
                    new RandomStringGenerator.Builder()
                            .withinRange('0', 'z')
                            .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
                            .build();
            token = randomStringGenerator.generate(8);
            tokenExists = this.userRegistrationRequestRepository.existsByToken(token);
        }
        newRequest.setToken(token);
        newRequest.setExpiresAt(new Date(System.currentTimeMillis() + registrationExpiryDuration));

        newRequest = this.userRegistrationRequestRepository.save(newRequest);
        return newRequest;
    }

    public boolean confirmRegistration(String token) {
        UserRegistrationRequest request = this.userRegistrationRequestRepository.findByToken(token);
        if (request == null) {
            return false;
        }
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

    @Scheduled(fixedRateString = "${user.registration.requestCleanupRate}")
    @Transactional
    public void cleanUpRegistrationRequests() {
        List<UserRegistrationRequest> toDelete = userRegistrationRequestRepository.findByExpiresAtIsBefore(new Date());
        int deleted = toDelete.size();
        userRepository.deleteAll(toDelete.stream().map(UserRegistrationRequest::getUser).collect(Collectors.toList()));
        userRegistrationRequestRepository.deleteAll(toDelete);
        log.info("{} User Registration requests were deleted", deleted);
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

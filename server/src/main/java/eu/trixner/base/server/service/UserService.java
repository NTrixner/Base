package eu.trixner.base.server.service;

import eu.trixner.base.dto.UserDto;
import eu.trixner.base.dto.UserListDto;
import eu.trixner.base.server.model.User;
import eu.trixner.base.server.repository.UserRepository;
import eu.trixner.base.server.service.mapper.UserListMapper;
import eu.trixner.base.server.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private UserListMapper userListMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, UserListMapper userListMapper){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userListMapper = userListMapper;
    }

    public UserListDto getUsers(Pageable paginationInfo){
        return userListMapper.mapPageToUserList(userRepository.findAll(paginationInfo).map(userMapper::userToUserDto));
    }

    public UserListDto getAllUsers() {
        UserListDto dto = new UserListDto();
        dto.setItems(StreamSupport.stream(userRepository.findAll().spliterator(), true)
                .map(userMapper::userToUserDto).collect(Collectors.toList()));
        return dto;
    }

    public Long countUsers(){
        return userRepository.count();
    }

    public UserDto findUser(UUID uuid) {
        return userRepository.findById(uuid).map(userMapper::userToUserDto).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String s)
    {
        User u = userRepository.findByUsernameIgnoreCase(s);
        if(u == null)
            throw new UsernameNotFoundException("User " + s + " was not found");
        return u;
    }
}

package eu.trixner.base.server.service;

import eu.trixner.base.dto.UserListDto;
import eu.trixner.base.server.repository.UserRepository;
import eu.trixner.base.server.service.mapper.UserListMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserListService {

    private UserRepository userRepository;
    private UserListMapper userListMapper;

    public UserListService(UserRepository userRepository, UserListMapper userListMapper) {
        this.userRepository = userRepository;
        this.userListMapper = userListMapper;
    }

    public Long countUsers() {
        return userRepository.count();
    }

    public UserListDto getUsers(Pageable paginationInfo) {
        return userListMapper.mapPageToUserList(userRepository.findAll(paginationInfo));
    }

    public UserListDto getAllUsers() {
        return userListMapper.mapPageToUserList(userRepository.findAll(Pageable.unpaged()));
    }
}

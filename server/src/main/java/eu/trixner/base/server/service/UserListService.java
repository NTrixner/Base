package eu.trixner.base.server.service;

import eu.trixner.base.dto.UserListDto;
import eu.trixner.base.server.repository.UserRepository;
import eu.trixner.base.server.service.mapper.UserListMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserListService {

    private final UserRepository userRepository;
    private final UserListMapper userListMapper;

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

package eu.trixner.base.server.service;

import eu.trixner.base.dto.UserTypeDto;
import eu.trixner.base.server.model.UserType;
import eu.trixner.base.server.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserMapper userTypeMapper;

    public List<UserTypeDto> getUserTypes() {
        return userTypeMapper.mapUserTypeDtos(Arrays.stream(UserType.values()).toList());
    }
}

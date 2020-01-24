package eu.trixner.base.server.service.mapper;

import eu.trixner.base.dto.UserListDto;
import eu.trixner.base.server.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserListMapper {

    private static final String USERNAME = "username";
    private UserMapper userMapper;

    @Autowired
    public UserListMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public UserListDto mapPageToUserList(Page<User> page) {
        UserListDto dto = new UserListDto();
        dto.setItems(page.get().map(userMapper::userToUserDto).collect(Collectors.toList()));
        if (page.getSort().isSorted()
                && page.getSort().getOrderFor(USERNAME) != null
                && page.getSort().getOrderFor(USERNAME).getDirection() != null) {
            dto.setOrdering(page.getSort().getOrderFor(USERNAME).getDirection().name());
        }
        dto.setPagePos(page.getNumber());
        dto.setPageSize(page.getSize());

        return dto;
    }
}

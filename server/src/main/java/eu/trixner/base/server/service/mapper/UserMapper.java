package eu.trixner.base.server.service.mapper;

import eu.trixner.base.dto.UserDto;
import eu.trixner.base.server.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userToUserDto(User userEntity);
}

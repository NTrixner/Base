package eu.trixner.base.server.service.mapper;

import eu.trixner.base.dto.RegistrationDto;
import eu.trixner.base.dto.UserDto;
import eu.trixner.base.server.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userToUserDto(User userEntity);

    @Mapping(target = "isActivated", constant = "false")
    @Mapping(target = "userType", constant = "USER")
    User userRegistrationDtoToUser(RegistrationDto dto);
}

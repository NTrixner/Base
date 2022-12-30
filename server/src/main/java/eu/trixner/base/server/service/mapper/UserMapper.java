package eu.trixner.base.server.service.mapper;

import eu.trixner.base.dto.RegistrationDto;
import eu.trixner.base.dto.UserDto;
import eu.trixner.base.dto.UserTypeDto;
import eu.trixner.base.server.model.User;
import eu.trixner.base.server.model.UserType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "type", source = "userType")
    UserDto userToUserDto(User userEntity);

    @Mapping(target = "isActivated", constant = "false")
    @Mapping(target = "userType", constant = "USER")
    User userRegistrationDtoToUser(RegistrationDto dto);

    User userDtoToUser(UserDto dto);

    @Mapping(target = "userType", source = "type")
    void updateUser(@MappingTarget User target, UserDto dto);

    List<UserTypeDto> mapUserTypeDtos(Collection<UserType> types);

    @Mapping(target = "name", expression = "java(userType.name())")
    @Mapping(target = "rights", source = "roles")
    @Mapping(target = "id", expression = "java(userType.ordinal())")
    UserTypeDto mapUserTypeDto(UserType userType);

    default UserType userTypeDtoToUserType(UserTypeDto dto) {
        return UserType.valueOf(dto.getName());
    }
}

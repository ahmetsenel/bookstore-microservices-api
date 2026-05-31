package com.ahmetsenel.userservice.mapper;


import com.ahmetsenel.userservice.dto.user.UserDetail;
import com.ahmetsenel.userservice.dto.user.UserRequest;
import com.ahmetsenel.userservice.dto.user.UserSummary;
import com.ahmetsenel.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(UserRequest userRequest);

    UserDetail toDetail(User user);

    UserSummary toSummary(User user);

    void updateUserFromDto(UserRequest dto, @MappingTarget User user);
}

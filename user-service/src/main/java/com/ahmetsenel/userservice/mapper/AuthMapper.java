package com.ahmetsenel.userservice.mapper;

import com.ahmetsenel.userservice.dto.auth.AuthRequest;
import com.ahmetsenel.userservice.dto.auth.RegisterResponse;
import com.ahmetsenel.userservice.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    User toEntity(AuthRequest authRequest);

    RegisterResponse toRegisterResponse(User user);
}

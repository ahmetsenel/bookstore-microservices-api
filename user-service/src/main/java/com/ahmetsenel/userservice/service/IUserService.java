package com.ahmetsenel.userservice.service;

import com.ahmetsenel.userservice.dto.user.UserDetail;
import com.ahmetsenel.userservice.dto.user.UserRequest;
import com.ahmetsenel.userservice.dto.user.UserSummary;
import com.ahmetsenel.userservice.entity.User;

import java.util.List;

public interface IUserService {

    User getUserById(Long id);

    List<UserSummary> getAllUsers();

    UserDetail getUserDetailById(Long id);

    UserDetail updateUser(Long userId, UserRequest userRequest);

    void deleteUser(Long id);
}

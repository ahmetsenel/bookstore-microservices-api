package com.ahmetsenel.userservice.service.impl;

import com.ahmetsenel.common.exception.BaseException;
import com.ahmetsenel.common.exception.MessageType;
import com.ahmetsenel.userservice.dto.user.UserDetail;
import com.ahmetsenel.userservice.dto.user.UserRequest;
import com.ahmetsenel.userservice.dto.user.UserSummary;
import com.ahmetsenel.userservice.entity.User;
import com.ahmetsenel.userservice.mapper.UserMapper;
import com.ahmetsenel.userservice.repository.UserRepository;
import com.ahmetsenel.userservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserSummary> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(userMapper::toSummary)
                .toList();
    }

    @Override
    public UserDetail getUserDetailById(Long id) {
        return userMapper.toDetail(getUserById(id));
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.USER_NOT_FOUND));
    }

    @Override
    public UserDetail updateUser(Long userId, UserRequest userRequest) {
        User user = getUserById(userId);
        userMapper.updateUserFromDto(userRequest, user);
        userRepository.save(user);
        return userMapper.toDetail(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}

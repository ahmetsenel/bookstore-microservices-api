package com.ahmetsenel.userservice.service.impl;

import com.ahmetsenel.common.exception.BaseException;
import com.ahmetsenel.common.exception.MessageType;
import com.ahmetsenel.security.jwt.JwtUtil;
import com.ahmetsenel.userservice.dto.auth.AuthRequest;
import com.ahmetsenel.userservice.dto.auth.LoginResponse;
import com.ahmetsenel.userservice.dto.auth.RegisterResponse;
import com.ahmetsenel.userservice.entity.Role;
import com.ahmetsenel.userservice.entity.User;
import com.ahmetsenel.userservice.mapper.AuthMapper;
import com.ahmetsenel.userservice.repository.UserRepository;
import com.ahmetsenel.userservice.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RegisterResponse register(AuthRequest authRequest) {
        if (userRepository.existsByUsername(authRequest.getUsername())) {
            throw new BaseException(MessageType.USERNAME_ALREADY_EXIST);
        }
        authRequest.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        User user = authMapper.toEntity(authRequest);
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
        return authMapper.toRegisterResponse(user);
    }

    @Override
    public LoginResponse login(AuthRequest authRequest) {
        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(()->new BaseException(MessageType.USER_NOT_FOUND));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new BaseException(MessageType.USER_NOT_FOUND);
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());

        String token = jwtUtil.generateToken(claims, user.getUsername());

        return new LoginResponse(token);
    }
}

package com.ahmetsenel.userservice.controller.user;

import com.ahmetsenel.common.response.ApiResponse;
import com.ahmetsenel.security.jwt.JwtUtil;
import com.ahmetsenel.userservice.dto.user.UserDetail;
import com.ahmetsenel.userservice.dto.user.UserRequest;
import com.ahmetsenel.userservice.service.impl.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/me")
    public ApiResponse<UserDetail> getUserDetail(@AuthenticationPrincipal Jwt jwt){
        Long userId = jwtUtil.getUserIdByToken(jwt.getTokenValue());
        return ApiResponse.success(userService.getUserDetailById(userId));
    }

    @PatchMapping()
    public ApiResponse<UserDetail> updateUser(@Valid @AuthenticationPrincipal Jwt jwt, @RequestBody UserRequest userRequest){
        Long userId = jwtUtil.getUserIdByToken(jwt.getTokenValue());
        return ApiResponse.success(userService.updateUser(userId, userRequest));
    }
}

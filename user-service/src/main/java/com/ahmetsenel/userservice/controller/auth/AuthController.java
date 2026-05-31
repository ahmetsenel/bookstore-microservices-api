package com.ahmetsenel.userservice.controller.auth;

import com.ahmetsenel.common.response.ApiResponse;
import com.ahmetsenel.userservice.dto.auth.AuthRequest;
import com.ahmetsenel.userservice.dto.auth.LoginResponse;
import com.ahmetsenel.userservice.dto.auth.RegisterResponse;
import com.ahmetsenel.userservice.service.impl.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(@Valid @RequestBody AuthRequest authRequest) {
        return ApiResponse.success(authService.register(authRequest));
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        return ApiResponse.success(authService.login(authRequest));
    }
}

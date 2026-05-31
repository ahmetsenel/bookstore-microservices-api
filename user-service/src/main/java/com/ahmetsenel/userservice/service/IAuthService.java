package com.ahmetsenel.userservice.service;

import com.ahmetsenel.userservice.dto.auth.AuthRequest;
import com.ahmetsenel.userservice.dto.auth.LoginResponse;
import com.ahmetsenel.userservice.dto.auth.RegisterResponse;

public interface IAuthService{

    RegisterResponse register(AuthRequest authRequest);

    LoginResponse login(AuthRequest authRequest);
}

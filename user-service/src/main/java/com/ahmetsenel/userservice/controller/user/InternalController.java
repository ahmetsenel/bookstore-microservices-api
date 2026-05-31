package com.ahmetsenel.userservice.controller.user;

import com.ahmetsenel.common.response.ApiResponse;
import com.ahmetsenel.userservice.dto.user.UserDetail;
import com.ahmetsenel.userservice.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/users")
public class InternalController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ApiResponse<UserDetail> getUserById(@PathVariable Long id) {
        return ApiResponse.success(userService.getUserDetailById(id));
    }
}

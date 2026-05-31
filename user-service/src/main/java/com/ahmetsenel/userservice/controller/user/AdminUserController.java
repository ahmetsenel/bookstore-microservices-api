package com.ahmetsenel.userservice.controller.user;

import com.ahmetsenel.common.response.ApiResponse;
import com.ahmetsenel.userservice.dto.user.UserDetail;
import com.ahmetsenel.userservice.dto.user.UserRequest;
import com.ahmetsenel.userservice.dto.user.UserSummary;
import com.ahmetsenel.userservice.service.impl.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public ApiResponse<List<UserSummary>> getAllUsers(){
        return ApiResponse.success(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDetail> getUserById(@PathVariable Long id) {
        return ApiResponse.success(userService.getUserDetailById(id));
    }

    @PatchMapping("/{id}")
    public ApiResponse<UserDetail> updateUserById(@Valid @PathVariable Long id, @RequestBody UserRequest userRequest){
        return ApiResponse.success(userService.updateUser(id, userRequest));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.success("User deleted successfully");
    }
}

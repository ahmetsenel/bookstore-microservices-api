package com.ahmetsenel.userservice.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest {

    @NotBlank(message = "First name cannot be empty")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    private String lastName;

    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Phone number cannot be empty)")
    @Size(min = 10, max = 10, message = "Phone number must be 9 characters")
    private String phoneNumber;

    @NotBlank(message = "Address cannot be empty")
    private String address;
}

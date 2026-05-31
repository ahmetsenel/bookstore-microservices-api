package com.ahmetsenel.userservice.dto.user;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({ "id", "firstName", "lastName", "username", "email", "phoneNumber" })
public class UserDetail {

    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String phoneNumber;

    private String address;
}

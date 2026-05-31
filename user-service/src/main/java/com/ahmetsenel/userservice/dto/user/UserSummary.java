package com.ahmetsenel.userservice.dto.user;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({ "id", "username", "firstName", "lastName", })
public class UserSummary {

    private Long id;

    private String username;

    private String firstName;

    private String lastName;
}

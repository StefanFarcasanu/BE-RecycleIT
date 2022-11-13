package com.domain.dto;

import com.domain.enums.RoleEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private Integer id;

    private String firstname;

    private String lastname;

    private String email;

    private String password;

    private String county;

    private String city;

    private RoleEnum role;
}

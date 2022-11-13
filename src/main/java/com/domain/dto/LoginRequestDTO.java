package com.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    private String email;
    private String password;

    public LoginRequestDTO(){

    }

    public LoginRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

package com.suika.englishlearning.model.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDto {
    private String name;
    private String email;
    private String role;
    private String accessToken;
    private String tokenType = "Bearer ";

    public AuthDto(String name, String email, String role, String accessToken) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.accessToken = accessToken;
    }

}

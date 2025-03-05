package com.suika.englishlearning.model.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDto {
    private String name;
    private String email;
    private String role;

    public AuthResponseDto(String name, String email, String role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }
}

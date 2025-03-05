package com.suika.englishlearning.model.dto.user;

import com.suika.englishlearning.model.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private String name;
    private String email;
    private String password;
    private Role role;
}

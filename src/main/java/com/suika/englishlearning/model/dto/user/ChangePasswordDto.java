package com.suika.englishlearning.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePasswordDto {
    private String email;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}

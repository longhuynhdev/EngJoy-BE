package com.suika.englishlearning.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class UpdateUserDto {
    private String name;
    private String email;
}

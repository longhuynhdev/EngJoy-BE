package com.suika.englishlearning.service;

import com.suika.englishlearning.exception.IncorrectPasswordException;
import com.suika.englishlearning.mapper.UserMapper;
import com.suika.englishlearning.model.UserEntity;
import com.suika.englishlearning.model.dto.user.UpdateUserDto;
import com.suika.englishlearning.model.dto.user.ChangePasswordDto;
import com.suika.englishlearning.model.dto.user.UserDto;
import com.suika.englishlearning.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserEntityService {
    public final UserRepository userRepository;
    public final UserMapper userMapper;
    @Autowired
    public PasswordEncoder passwordEncoder;

    public UserEntityService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userMapper = new UserMapper();
    }

    public List<UserDto> getUsers() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    public UserDto getUserByEmail(String email) {
        return userMapper.toDto(userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email)));
    }

    @Transactional
    public String updateUser(String currentEmail, UpdateUserDto updateUserDto) {
        UserEntity user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + currentEmail));

        String newEmail = updateUserDto.getEmail();
        String name = updateUserDto.getName();

        if (newEmail != null && !newEmail.isEmpty() && !Objects.equals(user.getEmail(), newEmail)) {
            user.setEmail(newEmail);
        }

        if (name != null && !name.isEmpty() && !Objects.equals(user.getName(), name)) {
            user.setName(name);
        }

        userRepository.save(user);
        return "Updated user successfully";
    }

    @Transactional
    public String updatePassword(ChangePasswordDto changePasswordDto) {
        String email = changePasswordDto.getEmail();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

        // Check if the current password matches
        String currentPassword = changePasswordDto.getCurrentPassword();
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IncorrectPasswordException("Current password is incorrect");
        }

        // Check if the new password and confirm password is matched
        String newPassword = changePasswordDto.getNewPassword();
        String confirmPassword = changePasswordDto.getConfirmPassword();
        if (!Objects.equals(newPassword, confirmPassword)) {
            throw new IncorrectPasswordException("Confirm password is not matched");
        }

        // Encode and set the new password, then save the user
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "Updated password successfully";
    }
}

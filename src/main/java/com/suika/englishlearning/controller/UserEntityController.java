package com.suika.englishlearning.controller;

import com.suika.englishlearning.exception.IncorrectPasswordException;
import com.suika.englishlearning.exception.NameException;
import com.suika.englishlearning.model.dto.user.UpdateUserDto;
import com.suika.englishlearning.model.dto.user.ChangePasswordDto;
import com.suika.englishlearning.model.dto.user.UserDto;
import com.suika.englishlearning.service.UserEntityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/userEntity")
public class UserEntityController {
    private final UserEntityService userEntityService;

    public UserEntityController(UserEntityService userEntityService) {
        this.userEntityService = userEntityService;
    }

    @GetMapping(path = "getUser/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email) {
        try {
            return new ResponseEntity<>(userEntityService.getUserByEmail(email), HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            return new ResponseEntity<>(userEntityService.getUserByEmail(userDetails.getUsername()), HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "getUsers")
    public ResponseEntity<List<UserDto>> getUsers() {
        return new ResponseEntity<>(userEntityService.getUsers(), HttpStatus.OK);
    }

    @PutMapping(path = "updateUser/{currentEmail}")
    public ResponseEntity<String> updateUser(@PathVariable("currentEmail") String currentEmail,
            @RequestBody UpdateUserDto updateUserDto) {
        try {
            String response = userEntityService.updateUser(currentEmail, updateUserDto);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (NameException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping(path = "updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        try {
            String response = userEntityService.updatePassword(changePasswordDto);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IncorrectPasswordException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

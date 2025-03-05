package com.suika.englishlearning.controller;

import com.suika.englishlearning.exception.IncorrectPasswordException;
import com.suika.englishlearning.exception.InvalidEmailException;
import com.suika.englishlearning.model.dto.auth.AuthDto;
import com.suika.englishlearning.model.dto.auth.AuthResponseDto;
import com.suika.englishlearning.model.dto.auth.LoginDto;
import com.suika.englishlearning.model.dto.auth.RegisterDto;
import com.suika.englishlearning.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        try {
            String response = authService.register(registerDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            AuthDto response = authService.login(loginDto);
            AuthResponseDto responseDto = new AuthResponseDto(response.getName(), response.getEmail(), response.getRole());

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("user", responseDto);
            responseMap.put("token", response.getAccessToken());

            return ResponseEntity.ok(responseMap);
        } catch (InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IncorrectPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logged out successfully");
    }

    // reference:
    // https://medium.com/@sallu-salman/implementing-sign-in-with-google-in-spring-boot-application-5f05a34905a8
    @GetMapping("googlegrantcode")
    public void grantCode(@RequestParam("code") String code, HttpServletResponse response) throws IOException, IOException {
        AuthDto authDto = authService.processGrantCode(code);
        String redirectUrl = "http://localhost:5173/auth/callback?token=" + authDto.getAccessToken() + "&name=" + authDto.getName() + "&email=" + authDto.getEmail() + "&role=" + authDto.getRole();
        response.sendRedirect(redirectUrl);
    }

    @ExceptionHandler({ InvalidEmailException.class, IncorrectPasswordException.class })
    public ResponseEntity<String> handleAuthExceptions(RuntimeException e) {
        if (e instanceof InvalidEmailException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } else if (e instanceof IncorrectPasswordException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }
}
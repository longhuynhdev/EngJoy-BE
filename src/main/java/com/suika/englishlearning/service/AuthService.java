package com.suika.englishlearning.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.suika.englishlearning.exception.IncorrectPasswordException;
import com.suika.englishlearning.exception.InvalidEmailException;
import com.suika.englishlearning.model.Role;
import com.suika.englishlearning.model.UserEntity;
import com.suika.englishlearning.model.dto.auth.AuthDto;
import com.suika.englishlearning.model.dto.auth.LoginDto;
import com.suika.englishlearning.model.dto.auth.RegisterDto;
import com.suika.englishlearning.model.dto.email.EmailDetails;
import com.suika.englishlearning.repository.RoleRepository;
import com.suika.englishlearning.repository.UserRepository;
import com.suika.englishlearning.security.JWTGenerator;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;
    private final EmailService emailService;


    @Value("${googleClientId}")
    String clientId;
    @Value("${googleClientSecret}")
    String clientSecret;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository,
                       RoleRepository roleRepository, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.emailService = emailService;
    }

    private boolean isNotValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        return !pattern.matcher(email).matches();
    }

    public String register(RegisterDto registerDto) {
        if (isNotValidEmail(registerDto.getEmail())) {
            throw new InvalidEmailException("Invalid email format");
        }
        if(userRepository.existsByEmail(registerDto.getEmail())) {
            throw new InvalidEmailException("Email address already in use");
        }
        UserEntity user = new UserEntity();
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        // Set role to USER
        Role role = roleRepository.findByName(registerDto.getRole())
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        user.setRole(role);

        userRepository.save(user);
        // Send email to user
        // Create email content
        String emailContent = "<html>" +
                "<body>" +
                "<h1>Welcome to JoyEng English Learning System</h1>" +
                "<p>You have successfully registered to JoyEng English Learning System.</p>" +
                "<a href='http://localhost:5173/' style='display: inline-block; padding: 10px 20px; font-size: 16px; color: white; background-color: #4CAF50; text-align: center; text-decoration: none; border-radius: 5px;'>Go to Home Page</a>" +
                "</body>" +
                "</html>";

        // Send email to user
        emailService.sendSimpleMail(new EmailDetails(user.getEmail(), "Welcome to JoyEng English Learning System", emailContent));
        return "User registered successfully";
    }

    public AuthDto processGrantCode(String code) {
        String accessToken = getOauthAccessTokenGoogle(code);
        UserEntity googleUser = getProfileDetailsGoogle(accessToken);
        Optional<UserEntity> userOptional = userRepository.findByEmail(googleUser.getEmail());

        UserEntity user;
        if (userOptional.isEmpty()) {
            RegisterDto registerDto = new RegisterDto(googleUser.getName(), googleUser.getEmail(), googleUser.getPassword());
            register(registerDto);
            user = userRepository.findByEmail(googleUser.getEmail()).orElseThrow(() -> new RuntimeException("User registration failed"));
        } else {
            user = userOptional.get();
        }

        // Directly set the authentication context for Google users
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);

        return new AuthDto(user.getName(), user.getEmail(), user.getRole().getName(), token);
    }

    private UserEntity getProfileDetailsGoogle(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);

        String url = "https://www.googleapis.com/oauth2/v2/userinfo";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        JsonObject jsonObject = new Gson().fromJson(response.getBody(), JsonObject.class);

        UserEntity user = new UserEntity();
        user.setEmail(jsonObject.get("email").toString().replace("\"", ""));
        user.setName(jsonObject.get("name").toString().replace("\"", ""));
        user.setPassword(UUID.randomUUID().toString());
        return user;
    }

    private String getOauthAccessTokenGoogle(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("redirect_uri", "http://localhost:8080/api/v1/auth/googlegrantcode");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile");
        params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email");
        params.add("scope", "openid");
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);

        String url = "https://oauth2.googleapis.com/token";
        String response = restTemplate.postForObject(url, requestEntity, String.class);
        JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);

        return jsonObject.get("access_token").toString().replace("\"", "");
    }

    public AuthDto login(LoginDto loginDto) {
        if (isNotValidEmail(loginDto.getEmail())) {
            throw new InvalidEmailException("Invalid email format");
        }
        if (!userRepository.existsByEmail(loginDto.getEmail())) {
            throw new InvalidEmailException("Incorrect email address or password");
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(),
                            loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);

            UserEntity user = userRepository.findByEmail(loginDto.getEmail()).get();
            String role = user.getRole().getName();

            return new AuthDto(user.getName(), user.getEmail(),role,token);

        } catch (Exception e) {
            throw new IncorrectPasswordException("Incorrect email address or password");
        }

    }
}

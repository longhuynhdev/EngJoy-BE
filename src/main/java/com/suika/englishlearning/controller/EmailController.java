package com.suika.englishlearning.controller;

import com.suika.englishlearning.model.dto.email.EmailDetails;
import com.suika.englishlearning.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/emails")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendSimpleMail(@RequestBody EmailDetails details) {
        String response = emailService.sendSimpleMail(details);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

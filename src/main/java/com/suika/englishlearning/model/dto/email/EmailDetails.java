package com.suika.englishlearning.model.dto.email;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmailDetails implements Serializable {

    // Email recipient
    private String recipient;

    // Subject of the email
    private String subject;

    // Message body of the email
    private String msgBody;


    // Constructors
    public EmailDetails() {
    }

    public EmailDetails(String recipient, String subject, String msgBody) {
        this.recipient = recipient;
        this.subject = subject;
        this.msgBody = msgBody;

    }

}

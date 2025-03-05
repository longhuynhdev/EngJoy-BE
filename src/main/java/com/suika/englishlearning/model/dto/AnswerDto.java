package com.suika.englishlearning.model.dto;

import lombok.Data;

@Data
public class AnswerDto {
    private int answerId;
    private String answer;
    private String explanation;
    private boolean isCorrect;
}

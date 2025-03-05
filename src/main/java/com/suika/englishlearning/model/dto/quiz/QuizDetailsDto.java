package com.suika.englishlearning.model.dto.quiz;

import com.suika.englishlearning.model.dto.question.QuestionDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuizDetailsDto {
    private int id;
    private String title;
    private String shortDescription;
    private int duration;
    private int points;
    private String body;
    private String author;
    private LocalDateTime date;
    private List<String> categories;
    private List<String> difficulties;
    private List<QuestionDto> questions;
}

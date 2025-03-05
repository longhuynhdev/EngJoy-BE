package com.suika.englishlearning.model.dto.quiz;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuizResponseDto {
    private int id;
    private String title;
    private String shortDescription;
    private int duration;
    private int points;
    private String author;
    private LocalDateTime date;
    private List<String> categories;
    private List<String> difficulties;
}

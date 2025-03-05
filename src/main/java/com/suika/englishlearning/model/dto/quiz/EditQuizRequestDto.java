package com.suika.englishlearning.model.dto.quiz;

import com.suika.englishlearning.model.dto.question.QuestionDto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class EditQuizRequestDto {
    private String title;
    private String shortDescription;
    private int duration;
    private int points;
    private LocalDateTime date;
    private List<String> categories;
    private List<String> difficulties;
}

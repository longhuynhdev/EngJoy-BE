package com.suika.englishlearning.model.dto.quiz;

import lombok.Data;

import java.util.List;

@Data
public class QuizQuestionsDto {
    private List<Integer> questionIds;
}

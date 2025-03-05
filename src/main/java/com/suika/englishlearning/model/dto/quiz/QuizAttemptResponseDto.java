package com.suika.englishlearning.model.dto.quiz;

import com.suika.englishlearning.model.dto.question.QuestionResultDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
public class QuizAttemptResponseDto {
    private int quizId;
    private String username;
    private LocalDateTime completedAt;
    private List<QuestionResultDto> questionResults;
    private Integer score;
}

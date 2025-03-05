package com.suika.englishlearning.model.dto.question;

import com.suika.englishlearning.model.dto.AnswerDto;
import lombok.Data;

import java.util.List;

@Data
public class QuestionDto {
    private int id;
    private String question;
    private List<AnswerDto> answers;
}

package com.suika.englishlearning.mapper;

import com.suika.englishlearning.model.Answer;
import com.suika.englishlearning.model.dto.AnswerDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AnswerMapper {
    public AnswerDto toAnswerDto(Answer answer) {
        AnswerDto dto = new AnswerDto();
        dto.setAnswerId(answer.getId());
        dto.setAnswer(answer.getAnswer());
        dto.setExplanation(answer.getExplanation());
        dto.setCorrect(answer.isCorrect());
        return dto;
    }

    public List<AnswerDto> toAnswerDtoList(List<Answer> answers) {
        return answers.stream().map(this::toAnswerDto).collect(Collectors.toList());
    }
}

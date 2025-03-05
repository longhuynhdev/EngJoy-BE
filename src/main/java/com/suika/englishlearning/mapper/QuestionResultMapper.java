package com.suika.englishlearning.mapper;

import com.suika.englishlearning.exception.ResourceNotFoundException;
import com.suika.englishlearning.model.QuestionResult;
import com.suika.englishlearning.model.dto.question.QuestionResultDto;
import com.suika.englishlearning.repository.AnswerRepository;
import com.suika.englishlearning.repository.QuestionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuestionResultMapper implements Mapper<QuestionResult, QuestionResultDto>{
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public QuestionResultMapper(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }
    @Override
    public QuestionResultDto toDto(QuestionResult entity) {
        QuestionResultDto dto = new QuestionResultDto();
        dto.setQuestionId(entity.getQuestion().getId());
        dto.setAnswerId(entity.getSelectedAnswer().getId());

        return dto;
    }

    @Override
    public QuestionResult toEntity(QuestionResultDto dto) {
        QuestionResult entity = new QuestionResult();
        entity.setQuestion(questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found")));
        entity.setSelectedAnswer(answerRepository.findById(dto.getAnswerId())
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found")));

        return entity;
    }

    @Override
    public List<QuestionResultDto> toDtoList(List<QuestionResult> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<QuestionResult> toEntityList(List<QuestionResultDto> dtoList) {
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }
}

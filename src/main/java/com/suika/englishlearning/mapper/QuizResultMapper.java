package com.suika.englishlearning.mapper;

import com.suika.englishlearning.model.QuizResult;
import com.suika.englishlearning.model.dto.quiz.QuizAttemptRequestDto;
import com.suika.englishlearning.model.dto.quiz.QuizAttemptResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuizResultMapper implements Mapper<QuizResult, QuizAttemptResponseDto> {
    private final QuestionResultMapper questionResultMapper;

    public QuizResultMapper(QuestionResultMapper questionResultMapper) {
        this.questionResultMapper = questionResultMapper;
    }

    @Override
    public QuizAttemptResponseDto toDto(QuizResult entity) {
        QuizAttemptResponseDto dto = new QuizAttemptResponseDto();
        dto.setQuizId(entity.getId());
        dto.setUsername(entity.getUser().getUsername());
        dto.setCompletedAt(entity.getCompletedAt());
        dto.setQuestionResults(questionResultMapper.toDtoList(entity.getQuestionResults()));
        dto.setScore(entity.getScore());

        return dto;
    }

    @Override
    public QuizResult toEntity(QuizAttemptResponseDto dto) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public QuizResult toEntity(QuizAttemptRequestDto dto) {
        QuizResult entity = new QuizResult();
        entity.setId(dto.getQuizId());
        entity.setCompletedAt(dto.getCompletedAt());
        entity.setQuestionResults(questionResultMapper.toEntityList(dto.getQuestionResults()));

        return entity;
    }

    @Override
    public List<QuizAttemptResponseDto> toDtoList(List<QuizResult> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<QuizResult> toEntityList(List<QuizAttemptResponseDto> dtoList) {
        throw new UnsupportedOperationException("Not implemented");
    }
}

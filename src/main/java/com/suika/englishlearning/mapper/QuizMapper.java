package com.suika.englishlearning.mapper;

import com.suika.englishlearning.exception.ResourceNotFoundException;
import com.suika.englishlearning.model.*;
import com.suika.englishlearning.model.dto.quiz.QuizDetailsDto;
import com.suika.englishlearning.model.dto.quiz.AddQuizRequestDto;
import com.suika.englishlearning.model.dto.quiz.QuizResponseDto;
import com.suika.englishlearning.repository.CategoryRepository;
import com.suika.englishlearning.repository.DifficultyRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuizMapper implements Mapper<Quiz, QuizResponseDto> {
    private final CategoryRepository categoryRepository;
    private final DifficultyRepository difficultyRepository;
    private final QuestionMapper questionMapper;

    public QuizMapper(CategoryRepository categoryRepository,
                      DifficultyRepository difficultyRepository,
                      QuestionMapper questionMapper) {
        this.categoryRepository = categoryRepository;
        this.difficultyRepository = difficultyRepository;
        this.questionMapper = questionMapper;
    }

    @Override
    public QuizResponseDto toDto(Quiz entity) {
        QuizResponseDto dto = new QuizResponseDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setShortDescription(entity.getShortDescription());
        dto.setDuration(entity.getDuration());
        dto.setPoints(entity.getPoints());
        dto.setAuthor(entity.getAuthor().getName());
        dto.setDate(entity.getDate());
        dto.setCategories(entity.getCategories().stream().map(Category::getName).collect(Collectors.toList()));
        dto.setDifficulties(entity.getDifficulties().stream().map(Difficulty::getName).collect(Collectors.toList()));

        return dto;
    }

    @Override
    public Quiz toEntity(QuizResponseDto dto)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<QuizResponseDto> toDtoList(List<Quiz> entityList)
    {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<Quiz> toEntityList(List<QuizResponseDto> dtoList)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Quiz toEntity(AddQuizRequestDto dto, UserEntity author)
    {
        Quiz quiz = new Quiz();
        quiz.setTitle(dto.getTitle());
        quiz.setShortDescription(dto.getShortDescription());
        quiz.setDuration(dto.getDuration());
        quiz.setPoints(dto.getPoints());
        quiz.setDate(dto.getDate());
        quiz.setAuthor(author);

        List<Category> categories = dto.getCategories().stream()
                .map(name -> categoryRepository.findByName(name)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Category not found: " + name)))
                .collect(Collectors.toList());
        quiz.setCategories(categories);

        List<Difficulty> difficulties = dto.getDifficulties().stream()
                .map(name -> difficultyRepository.findByName(name)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Difficulty not found: " + name)))
                .collect(Collectors.toList());
        quiz.setDifficulties(difficulties);

        return quiz;
    }

    public QuizDetailsDto toDtoDetails(Quiz entity) {
        QuizDetailsDto dto = new QuizDetailsDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setShortDescription(entity.getShortDescription());
        dto.setDuration(entity.getDuration());
        dto.setPoints(entity.getPoints());
        dto.setAuthor(entity.getAuthor().getName());
        dto.setDate(entity.getDate());
        dto.setCategories(entity.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toList()));
        dto.setDifficulties(entity.getDifficulties().stream()
                .map(Difficulty::getName)
                .collect(Collectors.toList()));
        dto.setQuestions(questionMapper.toDtoList(entity.getQuestions()));

        return dto;
    }
}

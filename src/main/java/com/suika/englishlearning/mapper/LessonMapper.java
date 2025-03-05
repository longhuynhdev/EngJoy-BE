package com.suika.englishlearning.mapper;

import com.suika.englishlearning.exception.ResourceNotFoundException;
import com.suika.englishlearning.model.*;
import com.suika.englishlearning.model.dto.lesson.LessonDetailsDto;
import com.suika.englishlearning.model.dto.lesson.LessonRequestDto;
import com.suika.englishlearning.model.dto.lesson.LessonResponseDto;

import com.suika.englishlearning.model.dto.question.QuestionDto;
import com.suika.englishlearning.repository.CategoryRepository;
import com.suika.englishlearning.repository.DifficultyRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LessonMapper implements Mapper<Lesson, LessonResponseDto> {
        private final CategoryRepository categoryRepository;
        private final DifficultyRepository difficultyRepository;
        private final AnswerMapper answerMapper;

        public LessonMapper(CategoryRepository categoryRepository, DifficultyRepository difficultyRepository, AnswerMapper answerMapper) {
                this.categoryRepository = categoryRepository;
                this.difficultyRepository = difficultyRepository;
                this.answerMapper = answerMapper;
        }

        @Override
        public LessonResponseDto toDto(Lesson entity) {
                LessonResponseDto dto = new LessonResponseDto();
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
                dto.setMediaUrl(entity.getMediaUrl());
                return dto;
        }

        @Override
        public Lesson toEntity(LessonResponseDto dto) {
                throw new UnsupportedOperationException("Not implemented");
        }

        @Override
        public List<LessonResponseDto> toDtoList(List<Lesson> entityList) {
                return entityList.stream().map(this::toDto).collect(Collectors.toList());
        }

        @Override
        public List<Lesson> toEntityList(List<LessonResponseDto> dtoList) {
                throw new UnsupportedOperationException("Not implemented");
        }

        public Lesson toEntity(LessonRequestDto dto, UserEntity author) {
                Lesson lesson = new Lesson();
                lesson.setTitle(dto.getTitle());
                lesson.setShortDescription(dto.getShortDescription());
                lesson.setDuration(dto.getDuration());
                lesson.setPoints(dto.getPoints());
                lesson.setBody(dto.getBody());
                lesson.setDate(dto.getDate());
                lesson.setAuthor(author);

                List<Category> categories = dto.getCategories().stream()
                        .map(name -> categoryRepository.findByName(name)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                        "Category not found: " + name)))
                        .collect(Collectors.toList());
                lesson.setCategories(categories);

                List<Difficulty> difficulties = dto.getDifficulties().stream()
                        .map(name -> difficultyRepository.findByName(name)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                        "Difficulty not found: " + name)))
                        .collect(Collectors.toList());
                lesson.setDifficulties(difficulties);

                lesson.setMediaUrl(dto.getMediaUrl());

                return lesson;
        }

        public QuestionDto toQuestionDto(Question question) {
                QuestionDto dto = new QuestionDto();
                dto.setId(question.getId());
                dto.setQuestion(question.getQuestion());
                dto.setAnswers(answerMapper.toAnswerDtoList(question.getAnswers()));
                return dto;
        }

        public List<QuestionDto> toQuestionDtoList(List<Question> questions) {
                return questions.stream().map(this::toQuestionDto).collect(Collectors.toList());
        }

        public LessonDetailsDto toDtoDetails(Lesson entity) {
                LessonDetailsDto dto = new LessonDetailsDto();
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
                dto.setBody(entity.getBody());
                dto.setQuestions(toQuestionDtoList(entity.getQuestions()));
                dto.setMediaUrl(entity.getMediaUrl());
                return dto;
        }
}

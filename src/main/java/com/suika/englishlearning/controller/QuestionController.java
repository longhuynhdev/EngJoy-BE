package com.suika.englishlearning.controller;

import com.suika.englishlearning.exception.ResourceNotFoundException;
import com.suika.englishlearning.model.dto.question.QuestionDto;
import com.suika.englishlearning.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public List<QuestionDto> getAllQuestions() {
        return questionService.getAllQuestion();
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDto> getQuestion(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(questionService.getQuestionById(id), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','CONTENT_EDITOR')")
    @PostMapping
    public ResponseEntity<List<QuestionDto>> createQuestions(@RequestBody List<QuestionDto> questionDtos) {
        return new ResponseEntity<>(questionService.createQuestions(questionDtos), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','CONTENT_EDITOR')")
    @PutMapping("/{id}")
    public ResponseEntity<QuestionDto> editQuestion(@PathVariable Integer id, @RequestBody QuestionDto questionDto) {
        try {
            QuestionDto updatedQuestion = questionService.editQuestion(id, questionDto);
            return new ResponseEntity<>(updatedQuestion, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Integer id) {
        try {
            questionService.DeleteQuestion(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

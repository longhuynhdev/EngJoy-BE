package com.suika.englishlearning.controller;

import com.suika.englishlearning.exception.ResourceNotFoundException;
import com.suika.englishlearning.model.dto.lesson.LessonDetailsDto;
import com.suika.englishlearning.model.dto.lesson.LessonRequestDto;
import com.suika.englishlearning.model.dto.lesson.LessonResponseDto;
import com.suika.englishlearning.service.LessonService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lessons")
public class LessonController {
    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping
    public ResponseEntity<List<LessonResponseDto>> getLessons() {
        return new ResponseEntity<>(lessonService.getLessons(), HttpStatus.OK);
    }

    // Assigning questions to lesson
    @PreAuthorize("hasAnyRole('ADMIN','CONTENT_EDITOR')")
    @PutMapping("/{lessonId}/questions")
    public ResponseEntity<?> assignQuestionsToLesson(@PathVariable Integer lessonId,
            @RequestBody List<Integer> questionIds) {
        try {
            lessonService.assignQuestionsToLesson(lessonId, questionIds);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonDetailsDto> getLesson(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(lessonService.getLesson(id), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<List<LessonResponseDto>> filterLessons(
            @RequestParam(required = false) List<String> categories,
            @RequestParam(required = false) List<String> difficulties) {
        try {
            return new ResponseEntity<>(lessonService.filterLessons(categories, difficulties), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','CONTENT_EDITOR')")
    @PostMapping
    public ResponseEntity<?> createLesson(@RequestBody LessonRequestDto requestDto) {
        try {
            return new ResponseEntity<>(lessonService.createLesson(requestDto), HttpStatus.CREATED);
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','CONTENT_EDITOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLesson(@PathVariable Integer id, @RequestBody LessonRequestDto requestDto) {
        try {
            return new ResponseEntity<>(lessonService.updateLesson(id, requestDto), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Integer id) {
        try {
            lessonService.deleteLesson(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

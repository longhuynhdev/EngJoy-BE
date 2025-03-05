package com.suika.englishlearning.controller;

import com.suika.englishlearning.exception.ResourceNotFoundException;
import com.suika.englishlearning.model.dto.difficulty.DifficultyDto;
import com.suika.englishlearning.service.DifficultyService;
import jakarta.persistence.EntityExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/difficulty")
public class DifficultyController {
    private final DifficultyService difficultyService;

    public DifficultyController(DifficultyService difficultyService) {
        this.difficultyService = difficultyService;
    }

    @GetMapping(path = "getDifficulties")
    public ResponseEntity<List<DifficultyDto>> getDifficulties() {
        return new ResponseEntity<>(difficultyService.getDifficulties(), HttpStatus.OK);
    }

    @GetMapping(path = "/{name}")
    public ResponseEntity<DifficultyDto> getDifficulty(@PathVariable("name") String name) {
        try {
            return new ResponseEntity<>(difficultyService.getDifficultyByName(name), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','CONTENT_EDITOR')")
    @PutMapping(path = "editDifficulty/{name}")
    public ResponseEntity<String> editDifficulty(@PathVariable("name") String name,
            @RequestBody DifficultyDto difficultyDto) {
        try {
            String response = difficultyService.editDifficulty(name, difficultyDto);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidParameterException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','CONTENT_EDITOR')")
    @PostMapping(path = "addDifficulty")
    public ResponseEntity<String> addDifficulty(@RequestBody DifficultyDto difficultyDto) {
        try {
            String response = difficultyService.addDifficulty(difficultyDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteDifficulty/{name}")
    public ResponseEntity<String> deleteDifficulty(@PathVariable("name") String name) {
        try {
            String response = difficultyService.deleteDifficulty(name);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

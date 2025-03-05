package com.suika.englishlearning.controller;

import com.suika.englishlearning.exception.ResourceNotFoundException;
import com.suika.englishlearning.model.dto.category.CategoryDto;
import com.suika.englishlearning.service.CategoryService;
import jakarta.persistence.EntityExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(path = "getCategories")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return new ResponseEntity<>(categoryService.getCategories(), HttpStatus.OK);
    }

    @GetMapping(path = "/{name}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable("name") String name) {
        try {
            return new ResponseEntity<>(categoryService.getCategoryByName(name), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','CONTENT_EDITOR')")
    @PutMapping(path = "editCategory/{name}")
    public ResponseEntity<String> editCategory(@PathVariable("name") String name,
            @RequestBody CategoryDto categoryDto) {
        try {
            String response = categoryService.editCategory(name, categoryDto);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidParameterException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','CONTENT_EDITOR')")
    @PostMapping(path = "addCategory")
    public ResponseEntity<String> addCategory(@RequestBody CategoryDto categoryDto) {
        try {
            String response = categoryService.addCategory(categoryDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteCategory/{name}")
    public ResponseEntity<String> deleteCategory(@PathVariable("name") String name) {
        try {
            String response = categoryService.deleteCategory(name);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

package com.suika.englishlearning.service;

import com.suika.englishlearning.exception.ResourceNotFoundException;
import com.suika.englishlearning.mapper.CategoryMapper;
import com.suika.englishlearning.model.Category;
import com.suika.englishlearning.model.dto.category.CategoryDto;
import com.suika.englishlearning.repository.CategoryRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = new CategoryMapper();
    }

    public List<CategoryDto> getCategories() {
        return categoryMapper.toDtoList(categoryRepository.findAll());
    }

    public CategoryDto getCategoryByName(String name) {
        return categoryMapper.toDto(categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found")));
    }

    @Transactional
    public String editCategory(String name, CategoryDto categoryDto) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        String editName = categoryDto.getName();
        if (editName != null &&
                !editName.isEmpty() &&
                !Objects.equals(category.getDescription(), editName)) {
            category.setName(editName);
        }
        else {
            throw new InvalidParameterException("Invalid category name");
        }

        String editDescription = categoryDto.getDescription();
        if (editDescription != null &&
                !editDescription.isEmpty() &&
                !Objects.equals(category.getDescription(), editDescription)) {
            category.setDescription(editDescription);
        }
        else {
            throw new InvalidParameterException("Invalid category description");
        }

        return "Edit successfully";
    }

    public String addCategory(CategoryDto categoryDto) {
        String name = categoryDto.getName();
        Optional<Category> category = categoryRepository.findByName(name);

        if(category.isPresent()) {
            throw new EntityExistsException("Category existed.");
        };

        Category newCategory = categoryMapper.toEntity(categoryDto);
        categoryRepository.save(newCategory);

        return "Added successfully";
    }

    public String deleteCategory(String name)
    {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        categoryRepository.delete(category);

        return "Deleted successfully";
    }
}

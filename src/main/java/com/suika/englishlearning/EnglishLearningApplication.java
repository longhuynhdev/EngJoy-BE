package com.suika.englishlearning;

import com.suika.englishlearning.model.Category;
import com.suika.englishlearning.model.Difficulty;
import com.suika.englishlearning.model.Role;
import com.suika.englishlearning.repository.CategoryRepository;
import com.suika.englishlearning.repository.DifficultyRepository;
import com.suika.englishlearning.repository.RoleRepository;
import com.suika.englishlearning.service.AuthService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootApplication
public class EnglishLearningApplication implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final DifficultyRepository difficultyRepository;
    private final AuthService authService;
    
    public EnglishLearningApplication(RoleRepository roleRepository, CategoryRepository categoryRepository, DifficultyRepository difficultyRepository, AuthService authService) {
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
        this.difficultyRepository = difficultyRepository;
        this.authService = authService;
    }

    public static void main(String[] args) {
        SpringApplication.run(EnglishLearningApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) {
        initRoles();
        initCategories();
        initDifficulties();
        initStaff();
    }
    
    private void initRoles() {
        List<Role> roles = List.of(
                new Role("USER"),
                new Role("CONTENT_EDITOR"),
                new Role("ADMIN")
        );

        for (Role role : roles) {
            if(roleRepository.findByName(role.getName()).isEmpty()) {
                roleRepository.save(role);
            }
        }
    }
    
    private void initCategories() {
        List<Category> categories = List.of(
                new Category(1,"Vocabulary", "Learn about vocabularies"),
                new Category(2,"Reading", "Learn about reading"),
                new Category(3,"Grammar", "Learn about grammar"),
                new Category(4,"Listening", "Learn about listening"),
                new Category(6,"Writing", "Learn about writing")
        );

        for (Category category : categories) {
            if(categoryRepository.findByName(category.getName()).isEmpty()) {
                categoryRepository.save(category);
            }
        }
    }
    
    private void initDifficulties() {
        List<Difficulty> difficulties = List.of(
                new Difficulty(1,"Beginner", "For beginners"),
                new Difficulty(2,"Intermediate", "For intermediate learners"),
                new Difficulty(3,"Advanced", "For advanced learners")
        );

        for (Difficulty difficulty : difficulties) {
            if(difficultyRepository.findByName(difficulty.getName()).isEmpty()) {
                difficultyRepository.save(difficulty);
            }
        }
    }
    
    private void initStaff() {
        authService.registerStaff("Admin", "admin.engjoy@gmail.com", "Demo123@", "ADMIN");
        authService.registerStaff("Content Editor", "contenteditor.engjoy@gmail.com", "Demo123@", "CONTENT_EDITOR");
    }
}
package com.suika.englishlearning;

import com.suika.englishlearning.model.Category;
import com.suika.englishlearning.model.Difficulty;
import com.suika.englishlearning.model.Role;
import com.suika.englishlearning.repository.CategoryRepository;
import com.suika.englishlearning.repository.DifficultyRepository;
import com.suika.englishlearning.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class EnglishLearningApplication implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final DifficultyRepository difficultyRepository;
    
    public EnglishLearningApplication(RoleRepository roleRepository, CategoryRepository categoryRepository, DifficultyRepository difficultyRepository) {
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
        this.difficultyRepository = difficultyRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(EnglishLearningApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // Create a list of default entities
        List<Role> roles = List.of(
                new Role("ROLE_USER"),
                new Role("ROLE_CONTENT_EDITOR"),
                new Role("ROLE_ADMIN")
        );
        List<Category> categories = List.of(
                new Category(1,"Vocabulary", "Learn about vocabularies"),
                new Category(2,"Reading", "Learn about reading"),
                new Category(3,"Grammar", "Learn about grammar"),
                new Category(4,"Listening", "Learn about listening"),
                new Category(6,"Writing", "Learn about writing")
        );

        List<Difficulty> difficulties = List.of(
                new Difficulty(1,"Beginner", "For beginners"),
                new Difficulty(2,"Intermediate", "For intermediate learners"),
                new Difficulty(3,"Advanced", "For advanced learners")
        );

        // Check if the entities already exist in the database
        for (Role role : roles) {
            if(roleRepository.findByName(role.getName()).isEmpty()) {
                roleRepository.save(role);
            }
        }
        for (Category category : categories) {
            if(categoryRepository.findByName(category.getName()).isEmpty()) {
                categoryRepository.save(category);
            }
        }

        for (Difficulty difficulty : difficulties) {
            if(difficultyRepository.findByName(difficulty.getName()).isEmpty()) {
                difficultyRepository.save(difficulty);
            }
        }
    }
}

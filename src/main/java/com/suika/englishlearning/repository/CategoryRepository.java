package com.suika.englishlearning.repository;

import com.suika.englishlearning.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name);
    Boolean existsByName(String name);
}

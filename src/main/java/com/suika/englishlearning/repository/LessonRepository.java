package com.suika.englishlearning.repository;

import com.suika.englishlearning.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    @Query("SELECT l FROM Lesson l JOIN l.categories c WHERE c.name IN :categories")
    List<Optional<Lesson>> findByCategories(@Param("categories") List<String> categories);

    @Query("SELECT l FROM Lesson l JOIN l.difficulties d WHERE d.name IN :difficulties")
    List<Optional<Lesson>> findByDifficulties(@Param("difficulties") List<String> difficulties);

    @Query("SELECT DISTINCT l FROM Lesson l JOIN l.categories c JOIN l.difficulties d " +
            "WHERE c.name IN :categories AND d.name IN :difficulties")
    List<Optional<Lesson>> findByCategoriesAndDifficulties(@Param("categories") List<String> categories,
                                                         @Param("difficulties") List<String> difficulties);
}

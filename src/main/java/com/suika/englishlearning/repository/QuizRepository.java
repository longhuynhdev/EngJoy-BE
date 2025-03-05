package com.suika.englishlearning.repository;

import com.suika.englishlearning.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    @Query("SELECT q FROM Quiz q JOIN q.categories c WHERE c.name IN :categories")
    List<Optional<Quiz>> findByCategories(@Param("categories") List<String> categories);

    @Query("SELECT q FROM Quiz q JOIN q.difficulties d WHERE d.name IN :difficulties")
    List<Optional<Quiz>> findByDifficulties(@Param("difficulties") List<String> difficulties);

    @Query("SELECT DISTINCT q FROM Quiz q JOIN q.categories c JOIN q.difficulties d " +
            "WHERE c.name IN :categories AND d.name IN :difficulties")
    List<Optional<Quiz>> findByCategoriesAndDifficulties(@Param("categories") List<String> categories,
                                               @Param("difficulties") List<String> difficulties);
}

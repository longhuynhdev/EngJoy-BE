package com.suika.englishlearning.repository;

import com.suika.englishlearning.model.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DifficultyRepository extends JpaRepository<Difficulty, Integer> {
    Optional<Difficulty> findByName(String name);
    Boolean existsByName(String name);
}

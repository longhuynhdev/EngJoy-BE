package com.suika.englishlearning.repository;

import com.suika.englishlearning.model.Quiz;
import com.suika.englishlearning.model.QuizResult;
import com.suika.englishlearning.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizResultRepository extends JpaRepository<QuizResult, Integer> {
    List<Optional<QuizResult>> findByQuizAndUser(Quiz quiz, UserEntity user);
    List<Optional<QuizResult>> findByUser(UserEntity user);
}

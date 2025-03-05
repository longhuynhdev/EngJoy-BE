package com.suika.englishlearning.repository;

import com.suika.englishlearning.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
}

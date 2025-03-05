package com.suika.englishlearning.repository;

import com.suika.englishlearning.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
}

package com.suika.englishlearning.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "quiz_results")
public class QuizResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private LocalDateTime completedAt; // When the quiz was completed

    @ElementCollection
    @CollectionTable(name = "quiz_result_answers", joinColumns = @JoinColumn(name = "quiz_result_id"))
    private List<QuestionResult> questionResults; // Question-level results

    // Derived property for the user's score
    public int getScore() {
        int score = 0;

        for (QuestionResult questionResult : questionResults) {
            // Get the correct answer for the question
            Answer correctAnswer = questionResult.getQuestion().getAnswers().stream()
                    .filter(Answer::isCorrect)
                    .findFirst()
                    .orElse(null);

            // Check if the selected answer matches the correct answer
            if (correctAnswer != null && correctAnswer.equals(questionResult.getSelectedAnswer())) {
                score++;
            }
        }

        return score;
    }

    // Derived property for the total score
    public int getTotalScore() {
        return quiz.getQuestions().size();
    }
}

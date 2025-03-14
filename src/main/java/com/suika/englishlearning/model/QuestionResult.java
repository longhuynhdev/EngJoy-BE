package com.suika.englishlearning.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Embeddable
public class QuestionResult {
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "selected_answer_id")
    private Answer selectedAnswer;
}

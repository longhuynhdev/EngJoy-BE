package com.suika.englishlearning.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String question;

    @OneToMany
    @JoinColumn(name = "question_id")
    private List<Answer> answers;

    @ManyToMany(mappedBy = "questions")
    private List<Lesson> lessons;
}

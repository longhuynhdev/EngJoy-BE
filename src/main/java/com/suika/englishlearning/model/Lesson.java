package com.suika.englishlearning.model;

import java.time.LocalDateTime;
import java.util.List;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "lessons")
public class Lesson {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @Column(columnDefinition = "TEXT", nullable = false)
        private String shortDescription;

        @Column(nullable = false)
        private String title;

        private int duration;

        private int points;

        @Column(columnDefinition = "TEXT", nullable = false)
        private String body;

        @ManyToMany
        @JoinTable(name = "lesson_categories", joinColumns = @JoinColumn(name = "lesson_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
        private List<Category> categories;

        @ManyToMany
        @JoinTable(name = "lesson_difficulties", joinColumns = @JoinColumn(name = "lesson_id"), inverseJoinColumns = @JoinColumn(name = "difficulty_id"))
        private List<Difficulty> difficulties;

        @ManyToMany
        @JoinTable(name = "lesson_questions", joinColumns = @JoinColumn(name = "lesson_id"), inverseJoinColumns = @JoinColumn(name = "question_id"))
        private List<Question> questions;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "author_id")
        private UserEntity author;

        @Column(nullable = false)
        private LocalDateTime date;

        @Nullable
        private String mediaUrl;

}

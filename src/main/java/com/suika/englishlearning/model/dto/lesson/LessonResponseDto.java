package com.suika.englishlearning.model.dto.lesson;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class LessonResponseDto {
    private int id;
    private String shortDescription;
    private int duration;
    private int points;
    private String title;
    private String author;
    private LocalDateTime date;
    private List<String> categories;
    private List<String> difficulties;
    private String mediaUrl;
}

package com.suika.englishlearning.model.dto.lesson;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class LessonRequestDto {
    private String title;
    private String shortDescription;
    private int duration;
    private int points;
    private String body;
    private LocalDateTime date;
    private List<String> categories;
    private List<String> difficulties;
    private String mediaUrl;
}

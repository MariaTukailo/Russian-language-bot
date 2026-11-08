package com.example.russianlanguagebot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicResponseDto {

    private Long id;
    private String title;
    private String description;
    private Long videoId;              // <-- ДОБАВИТЬ
    private String videoTitle;
    private Boolean passed;// <-- ДОБАВИТЬ
    private String videoUrl;           // <-- ДОБАВИТЬ
    private List<String> conspectUrls; // <-- ДОБАВИТЬ
    private List<TestResponseDto> tests;
}
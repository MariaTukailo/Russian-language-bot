package com.example.russianlanguagebot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Результат прохождения теста")
public class TestResultDto {

    @Schema(description = "Количество правильных ответов", example = "7")
    private Integer correctAnswers;

    @Schema(description = "Всего вопросов в тесте", example = "10")
    private Integer totalQuestions;

    @Schema(description = "Сообщение с результатом", example = "Правильных ответов: 7 из 10")
    private String message;

    @Schema(description = "Новые достижения, полученные за тест")
    private List<AchievementResponseDto> newAchievements;
}
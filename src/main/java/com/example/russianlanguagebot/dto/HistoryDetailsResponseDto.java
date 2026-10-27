package com.example.russianlanguagebot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Подробности попытки: счёт и разбор каждого ответа")
public class HistoryDetailsResponseDto {

    @Schema(description = "Id записи истории", example = "1")
    private Long id;

    @Schema(description = "Id теста", example = "1")
    private Long testId;

    @Schema(description = "Название теста", example = "ь и ъ")
    private String testTitle;

    @Schema(description = "Правильных ответов", example = "8")
    private Integer correctAnswers;

    @Schema(description = "Всего вопросов", example = "10")
    private Integer totalQuestions;

    @Schema(description = "Когда была попытка", example = "2026-10-10T14:30:00")
    private LocalDateTime completedAt;

    @Schema(description = "Разбор ответов по вопросам")
    private List<UserAnswerResponseDto> answers;
}
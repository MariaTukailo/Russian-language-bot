package com.example.russianlanguagebot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запись истории прохождения теста")
public class HistoryResponseDto {

    @Schema(description = "Id пройденного теста", example = "1")
    private Long id;

    @Schema(description = "Id теста", example = "1")
    private Long testId;

    @Schema(description = "Название теста", example = "ь и ъ")
    private String testTitle;

    @Schema(description = "Когда тест был пройден", example = "2026-10-10T14:30:00")
    private LocalDateTime completedAt;
}
package com.example.russianlanguagebot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Запрос на создание или изменение достижения")
public class AchievementResponseDto {

    @Schema(description = "Id достижения", example = "1")
    private Long id;

    @Schema(description = "Название достижения", example = "Знаток")
    private String name;

    @Schema(description = "Количество баллов для достижения", example = "50")
    private Integer numberPoints;
}

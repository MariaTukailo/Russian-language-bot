package com.example.russianlanguagebot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Запрос на создание или изменение достижения")
public class AchievementRequestDto {

    @Schema(description = "Название достижения", example = "Знаток")
    @NotNull(message = "Название обязательно")
    @NotBlank(message = "Название обязательно")
    private String name;

    @Schema(description = "Количество баллов для достижения", example = "50")
    @NotNull(message = "Количество очков обязательно")
    @Min(value = 0, message = "Очки не могут быть отрицательными")
    private Integer numberPoints;
}

package com.example.russianlanguagebot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ пользователя на один вопрос теста")
public class UserAnswerResponseDto {

    @Schema(description = "Текст вопроса", example = "Как пишется слово?")
    private String questionText;

    @Schema(description = "Что выбрал пользователь", example = "объем")
    private String selectedText;

    @Schema(description = "Правильный вариант", example = "объём")
    private String correctText;

    @Schema(description = "Ответ неверный", example = "false")
    private Boolean correct;
}
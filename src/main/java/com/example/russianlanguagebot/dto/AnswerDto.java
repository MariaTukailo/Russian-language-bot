package com.example.russianlanguagebot.dto;

import jakarta.validation.constraints.NotNull;
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
public class AnswerDto {

    @NotNull(message = "ID вопроса обязателен")
    private Long questionId;

    @NotNull(message = "ID выбранного варианта обязателен")
    private Long selectedOptionId;
}
package com.example.russianlanguagebot.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
public class QuestionRequestDto {

    @NotBlank(message = "Текст вопроса обязателен")
    @Size(max = 1000, message = "Текст вопроса не должен превышать 1000 символов")
    private String text;

    @NotEmpty(message = "Должен быть хотя бы один вариант ответа")
    @Valid
    private List<AnswerOptionRequestDto> options;
}
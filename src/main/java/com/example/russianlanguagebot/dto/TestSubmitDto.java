package com.example.russianlanguagebot.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class TestSubmitDto {

    @NotNull(message = "ID теста обязателен")
    private Long testId;

    @NotEmpty(message = "Нужно ответить хотя бы на один вопрос")
    @Valid
    private List<AnswerDto> answers;
}
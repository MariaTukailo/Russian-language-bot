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
public class TestRequestDto {

    @NotBlank(message = "Название теста обязательно")
    @Size(max = 255, message = "Название не должно превышать 255 символов")
    private String title;

    @NotEmpty(message = "Тест должен содержать хотя бы один вопрос")
    @Valid
    private List<QuestionRequestDto> questions;
}
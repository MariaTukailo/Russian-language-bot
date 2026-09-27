package com.example.russianlanguagebot.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class TopicRequestDto {

    @NotBlank(message = "Название темы обязательно")
    @Size(max = 255, message = "Название не должно превышать 255 символов")
    private String title;

    @NotNull(message = "Список тестов обязателен (может быть пустым)")
    @Valid
    private List<TestRequestDto> tests;
}
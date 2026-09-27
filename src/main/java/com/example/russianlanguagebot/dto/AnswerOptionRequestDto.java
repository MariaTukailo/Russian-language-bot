package com.example.russianlanguagebot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class AnswerOptionRequestDto {

    @NotBlank(message = "Текст варианта обязателен")
    @Size(max = 500, message = "Текст варианта не должен превышать 500 символов")
    private String text;

    private boolean correct;
}
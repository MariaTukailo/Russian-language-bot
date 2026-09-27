package com.example.russianlanguagebot.dto;

import jakarta.validation.Valid;
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
public class LearningMapRequestDto {

    @NotNull(message = "Список разделов обязателен (может быть пустым)")
    @Valid
    private List<MapSectionRequestDto> sections;
}
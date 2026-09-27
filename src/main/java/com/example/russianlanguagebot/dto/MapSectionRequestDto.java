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
public class MapSectionRequestDto {

    @NotNull(message = "ID видео обязателен")
    private Long videoId;

    @NotNull(message = "ID конспекта обязателен")
    private Long conspectDocumentId;

    @NotNull(message = "ID теста обязателен")
    private Long testId;
}
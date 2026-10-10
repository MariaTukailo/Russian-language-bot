package com.example.russianlanguagebot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Карта обучения с разделами")
public class MapResponseDto {

    @Schema(description = "Id карты", example = "1")
    private Long id;

    @Schema(description = "Разделы карты")
    private List<MapSectionResponseDto> sections;
}
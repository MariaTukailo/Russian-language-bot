package com.example.russianlanguagebot.dto;

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
public class MapSectionResponseDto {

    private Long id;

    private VideoDto video;

    private Long conspectDocumentId;

    private String conspectTitle;

    private Long testId;

    private String testTitle;

    private Boolean passed;
}
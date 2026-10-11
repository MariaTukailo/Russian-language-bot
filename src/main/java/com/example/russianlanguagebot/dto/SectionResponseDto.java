package com.example.russianlanguagebot.dto;

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
public class SectionResponseDto {

    private Long id;

    private String title;

    private String description;

    private Long videoId;

    private String videoTitle;

    private Long conspectDocumentId;

    private String conspectTitle;

    private Boolean passed;

    private List<TopicListItemDto> topics;
}
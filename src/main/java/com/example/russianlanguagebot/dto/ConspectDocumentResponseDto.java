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
public class ConspectDocumentResponseDto {

    private Long id;

    private String title;

    private String originalFileName;

    private int totalPages;

    private List<ConspectPageDto> pages;

    private List<ConspectTopicDto> topics;
}
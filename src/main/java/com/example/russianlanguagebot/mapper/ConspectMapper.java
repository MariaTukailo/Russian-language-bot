package com.example.russianlanguagebot.mapper;

import com.example.russianlanguagebot.dto.ConspectDocumentRequestDto;
import com.example.russianlanguagebot.dto.ConspectDocumentResponseDto;
import com.example.russianlanguagebot.dto.ConspectPageDto;
import com.example.russianlanguagebot.dto.ConspectTopicDto;
import com.example.russianlanguagebot.entity.ConspectDocument;
import com.example.russianlanguagebot.entity.ConspectPage;
import com.example.russianlanguagebot.entity.ConspectTopic;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConspectMapper {

    public ConspectDocument toEntity(ConspectDocumentRequestDto dto) {
        return ConspectDocument.builder()
                .title(dto.getTitle())
                .originalFileName(dto.getOriginalFileName())
                .totalPages(dto.getTotalPages())
                .build();
    }

    public ConspectDocumentResponseDto toDto(ConspectDocument entity) {
        List<ConspectPageDto> pages = entity.getPages() == null
                ? List.of()
                : entity.getPages().stream()
                .map(this::toPageDto)
                .toList();

        List<ConspectTopicDto> topics = entity.getTopics() == null
                ? List.of()
                : entity.getTopics().stream()
                .map(this::toTopicDto)
                .toList();

        return ConspectDocumentResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .originalFileName(entity.getOriginalFileName())
                .totalPages(entity.getTotalPages())
                .pages(pages)
                .topics(topics)
                .build();
    }

    public ConspectPageDto toPageDto(ConspectPage entity) {
        return ConspectPageDto.builder()
                .id(entity.getId())
                .pageNumber(entity.getPageNumber())
                .imageUrl(entity.getImageUrl())
                .build();
    }

    public ConspectTopicDto toTopicDto(ConspectTopic entity) {
        return ConspectTopicDto.builder()
                .id(entity.getId())
                .topicId(entity.getTopic() == null ? null : entity.getTopic().getId())
                .title(entity.getTitle())
                .startPage(entity.getStartPage())
                .endPage(entity.getEndPage())
                .build();
    }
}
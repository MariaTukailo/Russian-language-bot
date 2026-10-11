package com.example.russianlanguagebot.mapper;

import com.example.russianlanguagebot.dto.LearningMapRequestDto;
import com.example.russianlanguagebot.dto.LearningMapResponseDto;
import com.example.russianlanguagebot.dto.SectionResponseDto;
import com.example.russianlanguagebot.entity.Section;
import com.example.russianlanguagebot.entity.LearningMap;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MapMapper {

    private final SectionMapper sectionMapper;

    public MapMapper(SectionMapper sectionMapper) {
        this.sectionMapper = sectionMapper;
    }

    public LearningMap toEntity(LearningMapRequestDto dto) {
        List<Section> sections = dto.getSections() == null
                ? List.of()
                : dto.getSections().stream()
                .map(sectionMapper::toEntity)
                .toList();

        return LearningMap.builder()
                .sections(sections)
                .build();
    }

    public LearningMapResponseDto toDto(LearningMap entity) {
        List<SectionResponseDto> sections = entity.getSections() == null
                ? List.of()
                : entity.getSections().stream()
                .map(sectionMapper::toDto)
                .toList();

        return LearningMapResponseDto.builder()
                .id(entity.getId())
                .sections(sections)
                .build();
    }
}
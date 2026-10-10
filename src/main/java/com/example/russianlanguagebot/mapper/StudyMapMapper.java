package com.example.russianlanguagebot.mapper;

import com.example.russianlanguagebot.dto.MapResponseDto;
import com.example.russianlanguagebot.entity.StudyMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudyMapMapper {

    private final MapMapper mapSectionMapper;

    public MapResponseDto toDto(StudyMap map) {
        return MapResponseDto.builder()
                .id(map.getId())
                .sections(map.getSections().stream()
                        .map(mapSectionMapper::toSectionDto)
                        .toList())
                .build();
    }
}

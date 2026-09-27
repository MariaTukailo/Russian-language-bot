package com.example.russianlanguagebot.mapper;

import com.example.russianlanguagebot.dto.VideoDto;
import com.example.russianlanguagebot.entity.Video;
import org.springframework.stereotype.Component;

@Component
public class VideoMapper {

    public Video toEntity(VideoDto dto) {
        return Video.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .url(dto.getUrl())
                .build();
    }

    public VideoDto toDto(Video entity) {
        return VideoDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .url(entity.getUrl())
                .build();
    }
}
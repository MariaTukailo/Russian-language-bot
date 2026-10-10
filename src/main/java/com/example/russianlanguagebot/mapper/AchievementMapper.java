package com.example.russianlanguagebot.mapper;

import com.example.russianlanguagebot.dto.AchievementRequestDto;
import com.example.russianlanguagebot.dto.AchievementResponseDto;
import com.example.russianlanguagebot.entity.Achievement;
import org.springframework.stereotype.Component;

@Component
public class AchievementMapper {
    public Achievement toEntity(AchievementRequestDto dto) {
        return Achievement.builder()
                .name(dto.getName())
                .numberPoints(dto.getNumberPoints())
                .build();
    }

    public AchievementResponseDto toDto(Achievement achievement) {
        return AchievementResponseDto.builder()
                .id(achievement.getId())
                .name(achievement.getName())
                .numberPoints(achievement.getNumberPoints())
                .build();
    }
}

package com.example.russianlanguagebot.mapper;

import com.example.russianlanguagebot.dto.HistoryResponseDto;
import com.example.russianlanguagebot.entity.History;
import org.springframework.stereotype.Component;

@Component
public class HistoryMapper {

    public HistoryResponseDto toDto(History history) {
        return HistoryResponseDto.builder()
                .id(history.getId())
                .testId(history.getTest().getId())
                .testTitle(history.getTest().getTitle())
                .completedAt(history.getCompletedAt())
                .build();
    }
}
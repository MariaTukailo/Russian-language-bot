package com.example.russianlanguagebot.mapper;

import com.example.russianlanguagebot.dto.HistoryDetailsResponseDto;
import com.example.russianlanguagebot.dto.HistoryResponseDto;
import com.example.russianlanguagebot.entity.History;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoryMapper {

    private final UserAnswerMapper userAnswerMapper;

    public HistoryResponseDto toDto(History history) {
        return HistoryResponseDto.builder()
                .id(history.getId())
                .testId(history.getTest().getId())
                .testTitle(history.getTest().getTitle())
                .completedAt(history.getCompletedAt())
                .build();
    }

    public HistoryDetailsResponseDto toDetailsDto(History history) {
        return HistoryDetailsResponseDto.builder()
                .id(history.getId())
                .testId(history.getTest().getId())
                .testTitle(history.getTest().getTitle())
                .correctAnswers(history.getCorrectAnswers())
                .totalQuestions(history.getTotalQuestions())
                .completedAt(history.getCompletedAt())
                .answers(history.getAnswers().stream()
                        .map(userAnswerMapper::toDto)
                        .toList())
                .build();
    }
}
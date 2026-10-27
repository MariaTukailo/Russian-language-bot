package com.example.russianlanguagebot.mapper;

import com.example.russianlanguagebot.dto.UserAnswerResponseDto;
import com.example.russianlanguagebot.entity.UserAnswer;
import org.springframework.stereotype.Component;

@Component
public class UserAnswerMapper {

    public UserAnswerResponseDto toDto(UserAnswer entity) {
        return UserAnswerResponseDto.builder()
                .questionText(entity.getQuestionText())
                .selectedText(entity.getSelectedText())
                .correctText(entity.getCorrectText())
                .correct(entity.getCorrect())
                .build();
    }
}
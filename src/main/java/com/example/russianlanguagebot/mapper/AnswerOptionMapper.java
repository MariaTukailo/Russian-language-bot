package com.example.russianlanguagebot.mapper;

import com.example.russianlanguagebot.dto.AnswerOptionRequestDto;
import com.example.russianlanguagebot.dto.AnswerOptionResponseDto;
import com.example.russianlanguagebot.entity.AnswerOption;
import org.springframework.stereotype.Component;

@Component
public class AnswerOptionMapper {

    public AnswerOption toEntity(AnswerOptionRequestDto dto) {
        return AnswerOption.builder()
                .text(dto.getText())
                .correct(dto.isCorrect())
                .build();
    }

    public AnswerOptionResponseDto toDto(AnswerOption entity) {
        return AnswerOptionResponseDto.builder()
                .id(entity.getId())
                .text(entity.getText())
                .build();
    }
}

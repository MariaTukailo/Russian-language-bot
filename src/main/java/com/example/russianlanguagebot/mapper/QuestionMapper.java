package com.example.russianlanguagebot.mapper;

import com.example.russianlanguagebot.dto.AnswerOptionResponseDto;
import com.example.russianlanguagebot.dto.QuestionRequestDto;
import com.example.russianlanguagebot.dto.QuestionResponseDto;
import com.example.russianlanguagebot.entity.AnswerOption;
import com.example.russianlanguagebot.entity.Question;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestionMapper {

    private final AnswerOptionMapper answerOptionMapper;

    public QuestionMapper(AnswerOptionMapper answerOptionMapper) {
        this.answerOptionMapper = answerOptionMapper;
    }

    public Question toEntity(QuestionRequestDto dto) {
        List<AnswerOption> options = dto.getOptions().stream()
                .map(answerOptionMapper::toEntity)
                .toList();

        return Question.builder()
                .text(dto.getText())
                .options(options)
                .build();
    }

    public QuestionResponseDto toDto(Question entity) {
        List<AnswerOptionResponseDto> options = entity.getOptions() == null
                ? List.of()
                : entity.getOptions().stream()
                .map(answerOptionMapper::toDto)
                .toList();

        return QuestionResponseDto.builder()
                .id(entity.getId())
                .text(entity.getText())
                .options(options)
                .build();
    }
}
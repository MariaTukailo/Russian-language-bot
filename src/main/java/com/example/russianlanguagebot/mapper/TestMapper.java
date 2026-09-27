package com.example.russianlanguagebot.mapper;

import com.example.russianlanguagebot.dto.QuestionResponseDto;
import com.example.russianlanguagebot.dto.TestRequestDto;
import com.example.russianlanguagebot.dto.TestResponseDto;
import com.example.russianlanguagebot.entity.Question;
import com.example.russianlanguagebot.entity.Test;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestMapper {

    private final QuestionMapper questionMapper;

    public TestMapper(QuestionMapper questionMapper) {
        this.questionMapper = questionMapper;
    }

    public Test toEntity(TestRequestDto dto) {
        List<Question> questions = dto.getQuestions().stream()
                .map(questionMapper::toEntity)
                .toList();

        return Test.builder()
                .title(dto.getTitle())
                .passed(false)
                .questions(questions)
                .build();
    }

    public TestResponseDto toDto(Test entity) {
        List<QuestionResponseDto> questions = entity.getQuestions() == null
                ? List.of()
                : entity.getQuestions().stream()
                .map(questionMapper::toDto)
                .toList();

        return TestResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .passed(entity.getPassed())
                .questions(questions)
                .build();
    }
}
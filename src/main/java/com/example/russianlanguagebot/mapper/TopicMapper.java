package com.example.russianlanguagebot.mapper;

import com.example.russianlanguagebot.dto.TestResponseDto;
import com.example.russianlanguagebot.dto.TopicRequestDto;
import com.example.russianlanguagebot.dto.TopicResponseDto;
import com.example.russianlanguagebot.entity.Test;
import com.example.russianlanguagebot.entity.Topic;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TopicMapper {

    private final TestMapper testMapper;

    public TopicMapper(TestMapper testMapper) {
        this.testMapper = testMapper;
    }

    public Topic toEntity(TopicRequestDto dto) {
        List<Test> tests = dto.getTests() == null
                ? List.of()
                : dto.getTests().stream()
                .map(testMapper::toEntity)
                .toList();

        return Topic.builder()
                .title(dto.getTitle())
                .tests(tests)
                .build();
    }

    public TopicResponseDto toDto(Topic entity) {
        List<TestResponseDto> tests = entity.getTests() == null
                ? List.of()
                : entity.getTests().stream()
                .map(testMapper::toDto)
                .toList();

        return TopicResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .tests(tests)
                .build();
    }
}
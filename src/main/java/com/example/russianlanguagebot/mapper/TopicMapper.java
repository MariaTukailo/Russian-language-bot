package com.example.russianlanguagebot.mapper;

import com.example.russianlanguagebot.dto.TestResponseDto;
import com.example.russianlanguagebot.dto.TopicListItemDto;
import com.example.russianlanguagebot.dto.TopicRequestDto;
import com.example.russianlanguagebot.dto.TopicResponseDto;
import com.example.russianlanguagebot.entity.Section;
import com.example.russianlanguagebot.entity.Test;
import com.example.russianlanguagebot.entity.Topic;
import com.example.russianlanguagebot.exception.NotFoundException;
import com.example.russianlanguagebot.repository.SectionRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TopicMapper {

    private final TestMapper testMapper;
    private final SectionRepository sectionRepository;

    public TopicMapper(TestMapper testMapper, SectionRepository sectionRepository) {
        this.testMapper = testMapper;
        this.sectionRepository = sectionRepository;
    }

    public Topic toEntity(TopicRequestDto dto) {
        return toEntity(dto, null);
    }

    public Topic toEntity(TopicRequestDto dto, Long sectionId) {
        List<Test> tests = dto.getTests() == null
                ? List.of()
                : dto.getTests().stream()
                .map(testMapper::toEntity)
                .toList();

        Section section = null;
        if (sectionId != null) {
            section = sectionRepository.findById(sectionId)
                    .orElseThrow(() -> new NotFoundException("Раздел", sectionId));
        }

        return Topic.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .section(section)
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
                .description(entity.getDescription())
                .tests(tests)
                .build();
    }

    public TopicListItemDto toListItemDto(Topic entity) {
        int testsCount = entity.getTests() == null ? 0 : entity.getTests().size();

        return TopicListItemDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .testsCount(testsCount)
                .build();
    }
}
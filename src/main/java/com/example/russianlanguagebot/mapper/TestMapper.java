package com.example.russianlanguagebot.mapper;

import com.example.russianlanguagebot.dto.QuestionResponseDto;
import com.example.russianlanguagebot.dto.TestRequestDto;
import com.example.russianlanguagebot.dto.TestResponseDto;
import com.example.russianlanguagebot.entity.Question;
import com.example.russianlanguagebot.entity.Test;
import com.example.russianlanguagebot.entity.Topic;
import com.example.russianlanguagebot.exception.NotFoundException;
import com.example.russianlanguagebot.repository.TopicRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestMapper {

    private final QuestionMapper questionMapper;
    private final TopicRepository topicRepository;

    public TestMapper(QuestionMapper questionMapper, TopicRepository topicRepository) {
        this.questionMapper = questionMapper;
        this.topicRepository = topicRepository;
    }

    // Возвращаем старый метод, чтобы TopicMapper и другие места не ломались
    public Test toEntity(TestRequestDto dto) {
        return toEntity(dto, null);
    }

    public Test toEntity(TestRequestDto dto, Long topicId) {
        List<Question> questions = dto.getQuestions() == null ? List.of() : dto.getQuestions().stream()
                .map(questionMapper::toEntity)
                .toList();

        Topic topic = null;
        if (topicId != null) {
            topic = topicRepository.findById(topicId)
                    .orElseThrow(() -> new NotFoundException("Тема", topicId));
        }

        return Test.builder()
                .title(dto.getTitle())
                .topic(topic)
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
                .questions(questions)
                .topicId(entity.getTopic() == null ? null : entity.getTopic().getId())
                .topicTitle(entity.getTopic() == null ? null : entity.getTopic().getTitle())
                .build();
    }
}
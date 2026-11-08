package com.example.russianlanguagebot.mapper;

import com.example.russianlanguagebot.dto.SectionListItemDto;
import com.example.russianlanguagebot.dto.SectionRequestDto;
import com.example.russianlanguagebot.dto.SectionResponseDto;
import com.example.russianlanguagebot.dto.TopicListItemDto;
import com.example.russianlanguagebot.entity.Section;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SectionMapper {

    private final TopicMapper topicMapper;

    public SectionMapper(TopicMapper topicMapper) {
        this.topicMapper = topicMapper;
    }

    public Section toEntity(SectionRequestDto dto) {
        return Section.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                // .passed(false) <--- ЭТО УДАЛЯЕМ, ТАК КАК ПОЛЯ БОЛЬШЕ НЕТ В СУЩНОСТИ
                .build();
    }

    public SectionResponseDto toDto(Section entity) {
        List<TopicListItemDto> topics = entity.getTopics() == null
                ? List.of()
                : entity.getTopics().stream()
                .map(topicMapper::toListItemDto)
                .toList();

        // ВЫЧИСЛЯЕМ СТАТУС НА ЛЕТУ
        boolean isSectionPassed = !topics.isEmpty()
                && topics.stream().allMatch(t -> Boolean.TRUE.equals(t.getPassed()));

        return SectionResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .passed(isSectionPassed) // Отдаем вычисленный статус
                .topics(topics)
                .build();
    }

    public SectionListItemDto toListItemDto(Section entity) {
        int topicsCount = entity.getTopics() == null ? 0 : entity.getTopics().size();

        // ВЫЧИСЛЯЕМ СТАТУС ДЛЯ СПИСКА
        boolean isSectionPassed = entity.getTopics() != null
                && !entity.getTopics().isEmpty()
                && entity.getTopics().stream().allMatch(t -> Boolean.TRUE.equals(t.getPassed()));

        return SectionListItemDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .topicsCount(topicsCount)
                .passed(isSectionPassed) // Отдаем вычисленный статус
                .build();
    }
}
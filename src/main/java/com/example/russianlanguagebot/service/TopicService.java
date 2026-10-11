package com.example.russianlanguagebot.service;

import com.example.russianlanguagebot.dto.TopicListItemDto;
import com.example.russianlanguagebot.dto.TopicRequestDto;
import com.example.russianlanguagebot.dto.TopicResponseDto;
import com.example.russianlanguagebot.entity.Section;
import com.example.russianlanguagebot.entity.Topic;
import com.example.russianlanguagebot.exception.NotFoundException;
import com.example.russianlanguagebot.mapper.TopicMapper;
import com.example.russianlanguagebot.repository.SectionRepository;
import com.example.russianlanguagebot.repository.TopicRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final SectionRepository sectionRepository;
    private final TopicMapper topicMapper;

    public TopicService(TopicRepository topicRepository,
                        SectionRepository sectionRepository,
                        TopicMapper topicMapper) {
        this.topicRepository = topicRepository;
        this.sectionRepository = sectionRepository;
        this.topicMapper = topicMapper;
    }

    @Transactional(readOnly = true)
    public List<TopicResponseDto> findAll() {
        log.info("Запрос списка всех тем");
        return topicRepository.findAll().stream()
                .map(topicMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TopicListItemDto> findAllForList() {
        log.info("Запрос списка тем для админки");
        return topicRepository.findAll().stream()
                .map(topicMapper::toListItemDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TopicListItemDto> findBySectionId(Long sectionId) {
        log.info("Запрос списка тем раздела id={}", sectionId);
        return topicRepository.findAll().stream()
                .filter(t -> t.getSection() != null && t.getSection().getId().equals(sectionId))
                .map(topicMapper::toListItemDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public TopicResponseDto findById(Long id) {
        log.info("Поиск темы по id={}", id);
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Тема с id={} не найдена", id);
                    return new NotFoundException("Тема", id);
                });
        return topicMapper.toDto(topic);
    }

    @Transactional
    public TopicResponseDto save(TopicRequestDto dto, Long sectionId) {
        log.info("Сохранение темы: title={}, sectionId={}", dto.getTitle(), sectionId);
        Topic saved = topicRepository.save(topicMapper.toEntity(dto, sectionId));
        log.info("Тема сохранена с id={}", saved.getId());
        return topicMapper.toDto(saved);
    }

    @Transactional
    public TopicResponseDto update(Long id, TopicRequestDto dto, Long sectionId) {
        log.info("Обновление темы id={}, sectionId={}", id, sectionId);

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Тема с id={} не найдена при обновлении", id);
                    return new NotFoundException("Тема", id);
                });

        topic.setTitle(dto.getTitle());
        topic.setDescription(dto.getDescription());

        if (sectionId != null) {
            Section section = sectionRepository.findById(sectionId)
                    .orElseThrow(() -> new NotFoundException("Раздел", sectionId));
            topic.setSection(section);
        }

        Topic updated = topicRepository.save(topic);
        log.info("Тема id={} обновлена", id);
        return topicMapper.toDto(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Удаление темы id={}", id);
        if (!topicRepository.existsById(id)) {
            log.warn("Нечего удалять — тема id={} не найдена", id);
            throw new NotFoundException("Тема", id);
        }
        topicRepository.deleteById(id);
        log.info("Тема id={} удалена", id);
    }
}
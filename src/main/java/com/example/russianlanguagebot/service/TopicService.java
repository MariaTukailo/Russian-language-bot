package com.example.russianlanguagebot.service;

import com.example.russianlanguagebot.dto.TopicRequestDto;
import com.example.russianlanguagebot.dto.TopicResponseDto;
import com.example.russianlanguagebot.entity.Topic;
import com.example.russianlanguagebot.exception.NotFoundException;
import com.example.russianlanguagebot.mapper.TopicMapper;
import com.example.russianlanguagebot.repository.TopicRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    public TopicService(TopicRepository topicRepository, TopicMapper topicMapper) {
        this.topicRepository = topicRepository;
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
    public TopicResponseDto save(TopicRequestDto dto) {
        log.info("Сохранение темы: title={}", dto.getTitle());
        Topic saved = topicRepository.save(topicMapper.toEntity(dto));
        log.info("Тема сохранена с id={}", saved.getId());
        return topicMapper.toDto(saved);
    }

    @Transactional
    public TopicResponseDto update(Long id, TopicRequestDto dto) {
        log.info("Обновление темы id={}", id);

        if (!topicRepository.existsById(id)) {
            log.warn("Тема с id={} не найдена при обновлении", id);
            throw new NotFoundException("Тема", id);
        }

        Topic toSave = topicMapper.toEntity(dto);
        toSave.setId(id);
        Topic updated = topicRepository.save(toSave);
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
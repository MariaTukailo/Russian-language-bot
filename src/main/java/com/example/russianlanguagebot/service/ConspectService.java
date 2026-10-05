package com.example.russianlanguagebot.service;

import com.example.russianlanguagebot.dto.ConspectDocumentRequestDto;
import com.example.russianlanguagebot.dto.ConspectDocumentResponseDto;
import com.example.russianlanguagebot.entity.ConspectDocument;
import com.example.russianlanguagebot.entity.ConspectTopic;
import com.example.russianlanguagebot.exception.NotFoundException;
import com.example.russianlanguagebot.mapper.ConspectMapper;
import com.example.russianlanguagebot.repository.ConspectDocumentRepository;
import com.example.russianlanguagebot.repository.ConspectTopicRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ConspectService {

    private final ConspectDocumentRepository documentRepository;
    private final ConspectTopicRepository topicRepository;
    private final ConspectMapper conspectMapper;

    public ConspectService(ConspectDocumentRepository documentRepository,
                           ConspectTopicRepository topicRepository,
                           ConspectMapper conspectMapper) {
        this.documentRepository = documentRepository;
        this.topicRepository = topicRepository;
        this.conspectMapper = conspectMapper;
    }

    @Transactional(readOnly = true)
    public List<ConspectDocumentResponseDto> findAll() {
        log.info("Запрос списка всех конспектов");
        return documentRepository.findAll().stream()
                .map(conspectMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ConspectDocumentResponseDto findById(Long id) {
        log.info("Поиск конспекта по id={}", id);
        ConspectDocument document = documentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Конспект с id={} не найден", id);
                    return new NotFoundException("Конспект", id);
                });
        return conspectMapper.toDto(document);
    }

    @Transactional
    public ConspectDocumentResponseDto save(ConspectDocumentRequestDto dto) {
        log.info("Сохранение конспекта: title={}", dto.getTitle());
        ConspectDocument saved = documentRepository.save(conspectMapper.toEntity(dto));
        log.info("Конспект сохранён с id={}", saved.getId());
        return conspectMapper.toDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Удаление конспекта id={}", id);
        if (!documentRepository.existsById(id)) {
            log.warn("Нечего удалять — конспект id={} не найден", id);
            throw new NotFoundException("Конспект", id);
        }
        documentRepository.deleteById(id);
        log.info("Конспект id={} удалён", id);
    }

    @Transactional
    public void deleteTopic(Long conspectTopicId) {
        log.info("Удаление темы конспекта id={}", conspectTopicId);
        if (!topicRepository.existsById(conspectTopicId)) {
            log.warn("Нечего удалять — тема конспекта id={} не найдена", conspectTopicId);
            throw new NotFoundException("Тема конспекта", conspectTopicId);
        }
        topicRepository.deleteById(conspectTopicId);
        log.info("Тема конспекта id={} удалена", conspectTopicId);
    }
g
    @Transactional(readOnly = true)
    public List<String> getPagesForTopic(Long conspectTopicId) {
        log.info("Запрос страниц для темы конспекта id={}", conspectTopicId);

        ConspectTopic conspectTopic = topicRepository.findById(conspectTopicId)
                .orElseThrow(() -> {
                    log.warn("Тема конспекта id={} не найдена", conspectTopicId);
                    return new NotFoundException("Тема конспекта", conspectTopicId);
                });

        ConspectDocument document = conspectTopic.getDocument();
        if (document == null) {
            log.warn("У темы конспекта id={} не указан документ", conspectTopicId);
            return List.of();
        }

        return document.getPages().stream()
                .filter(page -> page.getPageNumber() >= conspectTopic.getStartPage()
                        && page.getPageNumber() <= conspectTopic.getEndPage())
                .sorted((a, b) -> Integer.compare(a.getPageNumber(), b.getPageNumber()))
                .map(page -> page.getImageUrl())
                .toList();
    }
}
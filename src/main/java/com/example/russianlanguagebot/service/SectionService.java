package com.example.russianlanguagebot.service;

import com.example.russianlanguagebot.dto.SectionListItemDto;
import com.example.russianlanguagebot.dto.SectionRequestDto;
import com.example.russianlanguagebot.dto.SectionResponseDto;
import com.example.russianlanguagebot.entity.LearningMap;
import com.example.russianlanguagebot.entity.Section;
import com.example.russianlanguagebot.exception.NotFoundException;
import com.example.russianlanguagebot.mapper.SectionMapper;
import com.example.russianlanguagebot.repository.SectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class SectionService {

    private final SectionRepository sectionRepository;
    private final SectionMapper sectionMapper;
    private final MapService mapService;

    public SectionService(SectionRepository sectionRepository,
                          SectionMapper sectionMapper,
                          MapService mapService) {
        this.sectionRepository = sectionRepository;
        this.sectionMapper = sectionMapper;
        this.mapService = mapService;
    }

    @Transactional(readOnly = true)
    public List<SectionListItemDto> findAllForList() {
        log.info("Запрос списка разделов для админки");
        return sectionRepository.findAll().stream()
                .map(sectionMapper::toListItemDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SectionResponseDto> findAll() {
        log.info("Запрос списка всех разделов");
        return sectionRepository.findAll().stream()
                .map(sectionMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public SectionResponseDto findById(Long id) {
        log.info("Поиск раздела по id={}", id);
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Раздел с id={} не найден", id);
                    return new NotFoundException("Раздел", id);
                });
        return sectionMapper.toDto(section);
    }

    @Transactional
    public SectionResponseDto save(SectionRequestDto dto) {
        log.info("Сохранение раздела: title={}", dto.getTitle());

        LearningMap map = mapService.getSingleMapOrThrow();
        Section section = sectionMapper.toEntity(dto);
        section.setLearningMap(map);

        Section saved = sectionRepository.save(section);
        log.info("Раздел сохранён с id={}", saved.getId());
        return sectionMapper.toDto(saved);
    }

    @Transactional
    public SectionResponseDto markPassed(Long sectionId) {
        log.info("Отметка о прохождении раздела id={}", sectionId);

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new NotFoundException("Раздел", sectionId));

        section.setPassed(true);
        Section saved = sectionRepository.save(section);
        return sectionMapper.toDto(saved);
    }

    @Transactional
    public SectionResponseDto update(Long id, SectionRequestDto dto) {
        log.info("Обновление раздела id={}", id);

        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Раздел с id={} не найден при обновлении", id);
                    return new NotFoundException("Раздел", id);
                });

        section.setTitle(dto.getTitle());
        section.setDescription(dto.getDescription());

        Section updated = sectionRepository.save(section);
        log.info("Раздел id={} обновлён", id);
        return sectionMapper.toDto(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Удаление раздела id={}", id);
        if (!sectionRepository.existsById(id)) {
            log.warn("Нечего удалять — раздел id={} не найден", id);
            throw new NotFoundException("Раздел", id);
        }
        sectionRepository.deleteById(id);
        log.info("Раздел id={} удалён", id);
    }
}
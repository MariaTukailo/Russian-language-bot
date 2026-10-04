package com.example.russianlanguagebot.service;

import com.example.russianlanguagebot.dto.LearningMapRequestDto;
import com.example.russianlanguagebot.dto.LearningMapResponseDto;
import com.example.russianlanguagebot.dto.MapSectionRequestDto;
import com.example.russianlanguagebot.dto.MapSectionResponseDto;
import com.example.russianlanguagebot.entity.LearningMap;
import com.example.russianlanguagebot.entity.MapSection;
import com.example.russianlanguagebot.exception.ConflictException;
import com.example.russianlanguagebot.exception.NotFoundException;
import com.example.russianlanguagebot.mapper.MapMapper;
import com.example.russianlanguagebot.repository.LearningMapRepository;
import com.example.russianlanguagebot.repository.MapSectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class MapService {

    private final LearningMapRepository mapRepository;
    private final MapSectionRepository sectionRepository;
    private final MapMapper mapMapper;

    public MapService(LearningMapRepository mapRepository,
                      MapSectionRepository sectionRepository,
                      MapMapper mapMapper) {
        this.mapRepository = mapRepository;
        this.sectionRepository = sectionRepository;
        this.mapMapper = mapMapper;
    }

    @Transactional(readOnly = true)
    public List<LearningMapResponseDto> findAll() {
        log.info("Запрос списка всех карт");
        return mapRepository.findAll().stream()
                .map(mapMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public LearningMapResponseDto findById(Long id) {
        log.info("Поиск карты по id={}", id);
        LearningMap map = mapRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Карта с id={} не найдена", id);
                    return new NotFoundException("Карта", id);
                });
        return mapMapper.toDto(map);
    }

    @Transactional
    public LearningMapResponseDto save(LearningMapRequestDto dto) {
        log.info("Сохранение карты, разделов: {}", dto.getSections().size());

        if (mapRepository.count() > 0) {
            log.warn("Попытка создать вторую карту — запрещено");
            throw new ConflictException("Карта уже существует. Допустима только одна карта в системе");
        }

        LearningMap saved = mapRepository.save(mapMapper.toEntity(dto));
        log.info("Карта сохранена с id={}", saved.getId());
        return mapMapper.toDto(saved);
    }

    @Transactional
    public MapSectionResponseDto saveSection(Long mapId, MapSectionRequestDto dto) {
        log.info("Сохранение раздела карты для карты id={}", mapId);
        MapSection saved = sectionRepository.save(mapMapper.toSectionEntity(dto, mapId));
        log.info("Раздел карты сохранён с id={}", saved.getId());
        return mapMapper.toSectionDto(saved);
    }

    @Transactional
    public MapSectionResponseDto markSectionPassed(Long sectionId) {
        log.info("Отметка о прохождении раздела карты id={}", sectionId);

        MapSection section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> {
                    log.warn("Раздел карты id={} не найден", sectionId);
                    return new NotFoundException("Раздел карты", sectionId);
                });

        section.setPassed(true);
        MapSection saved = sectionRepository.save(section);
        log.info("Раздел карты id={} отмечен как пройденный", sectionId);
        return mapMapper.toSectionDto(saved);
    }

    @Transactional
    public void deleteSection(Long sectionId) {
        log.info("Удаление раздела карты id={}", sectionId);
        if (!sectionRepository.existsById(sectionId)) {
            log.warn("Нечего удалять — раздел карты id={} не найден", sectionId);
            throw new NotFoundException("Раздел карты", sectionId);
        }
        sectionRepository.deleteById(sectionId);
        log.info("Раздел карты id={} удалён", sectionId);
    }
}
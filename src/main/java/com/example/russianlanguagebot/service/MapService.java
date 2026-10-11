package com.example.russianlanguagebot.service;

import com.example.russianlanguagebot.dto.LearningMapRequestDto;
import com.example.russianlanguagebot.dto.LearningMapResponseDto;
import com.example.russianlanguagebot.entity.LearningMap;
import com.example.russianlanguagebot.exception.ConflictException;
import com.example.russianlanguagebot.exception.NotFoundException;
import com.example.russianlanguagebot.mapper.MapMapper;
import com.example.russianlanguagebot.repository.LearningMapRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class MapService {

    private final LearningMapRepository mapRepository;
    private final MapMapper mapMapper;

    public MapService(LearningMapRepository mapRepository, MapMapper mapMapper) {
        this.mapRepository = mapRepository;
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

    @Transactional(readOnly = true)
    public LearningMap getSingleMapOrThrow() {
        return mapRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Карта ещё не создана", 0L));
    }
}
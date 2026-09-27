package com.example.russianlanguagebot.service;

import com.example.russianlanguagebot.dto.VideoDto;
import com.example.russianlanguagebot.entity.Video;
import com.example.russianlanguagebot.exception.NotFoundException;
import com.example.russianlanguagebot.mapper.VideoMapper;
import com.example.russianlanguagebot.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class VideoService {

    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;

    public VideoService(VideoRepository videoRepository, VideoMapper videoMapper) {
        this.videoRepository = videoRepository;
        this.videoMapper = videoMapper;
    }

    @Transactional(readOnly = true)
    public List<VideoDto> findAll() {
        log.info("Запрос списка всех видео");
        return videoRepository.findAll().stream()
                .map(videoMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public VideoDto findById(Long id) {
        log.info("Поиск видео по id={}", id);
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Видео с id={} не найдено", id);
                    return new NotFoundException("Видео", id);
                });
        return videoMapper.toDto(video);
    }

    @Transactional
    public VideoDto save(VideoDto dto) {
        log.info("Сохранение видео: title={}", dto.getTitle());
        Video saved = videoRepository.save(videoMapper.toEntity(dto));
        log.info("Видео сохранено с id={}", saved.getId());
        return videoMapper.toDto(saved);
    }

    @Transactional
    public VideoDto update(Long id, VideoDto dto) {
        log.info("Обновление видео id={}", id);

        if (!videoRepository.existsById(id)) {
            log.warn("Видео с id={} не найдено при обновлении", id);
            throw new NotFoundException("Видео", id);
        }

        Video toSave = videoMapper.toEntity(dto);
        toSave.setId(id);
        Video updated = videoRepository.save(toSave);
        log.info("Видео id={} обновлено", id);
        return videoMapper.toDto(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Удаление видео id={}", id);
        if (!videoRepository.existsById(id)) {
            log.warn("Нечего удалять — видео id={} не найдено", id);
            throw new NotFoundException("Видео", id);
        }
        videoRepository.deleteById(id);
        log.info("Видео id={} удалено", id);
    }
}
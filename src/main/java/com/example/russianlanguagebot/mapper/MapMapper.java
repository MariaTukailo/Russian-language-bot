package com.example.russianlanguagebot.mapper;

import com.example.russianlanguagebot.dto.LearningMapRequestDto;
import com.example.russianlanguagebot.dto.LearningMapResponseDto;
import com.example.russianlanguagebot.dto.MapSectionRequestDto;
import com.example.russianlanguagebot.dto.MapSectionResponseDto;
import com.example.russianlanguagebot.entity.ConspectDocument;
import com.example.russianlanguagebot.entity.LearningMap;
import com.example.russianlanguagebot.entity.MapSection;
import com.example.russianlanguagebot.entity.Test;
import com.example.russianlanguagebot.entity.Video;
import com.example.russianlanguagebot.exception.NotFoundException;
import com.example.russianlanguagebot.repository.ConspectDocumentRepository;
import com.example.russianlanguagebot.repository.TestRepository;
import com.example.russianlanguagebot.repository.VideoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MapMapper {

    private final VideoMapper videoMapper;
    private final VideoRepository videoRepository;
    private final ConspectDocumentRepository documentRepository;
    private final TestRepository testRepository;

    public MapMapper(VideoMapper videoMapper,
                     VideoRepository videoRepository,
                     ConspectDocumentRepository documentRepository,
                     TestRepository testRepository) {
        this.videoMapper = videoMapper;
        this.videoRepository = videoRepository;
        this.documentRepository = documentRepository;
        this.testRepository = testRepository;
    }

    public LearningMap toEntity(LearningMapRequestDto dto) {
        List<MapSection> sections = dto.getSections() == null
                ? List.of()
                : dto.getSections().stream()
                .map(this::toSectionEntity)
                .toList();

        return LearningMap.builder()
                .sections(sections)
                .build();
    }

    public MapSection toSectionEntity(MapSectionRequestDto dto) {
        Video video = videoRepository.findById(dto.getVideoId())
                .orElseThrow(() -> new NotFoundException("Видео", dto.getVideoId()));

        ConspectDocument document = documentRepository.findById(dto.getConspectDocumentId())
                .orElseThrow(() -> new NotFoundException("Конспект", dto.getConspectDocumentId()));

        Test test = testRepository.findById(dto.getTestId())
                .orElseThrow(() -> new NotFoundException("Тест", dto.getTestId()));

        return MapSection.builder()
                .video(video)
                .conspectDocument(document)
                .test(test)
                .passed(false)
                .build();
    }

    public LearningMapResponseDto toDto(LearningMap entity) {
        List<MapSectionResponseDto> sections = entity.getSections() == null
                ? List.of()
                : entity.getSections().stream()
                .map(this::toSectionDto)
                .toList();

        return LearningMapResponseDto.builder()
                .id(entity.getId())
                .sections(sections)
                .build();
    }

    public MapSectionResponseDto toSectionDto(MapSection entity) {
        return MapSectionResponseDto.builder()
                .id(entity.getId())
                .video(entity.getVideo() == null ? null : videoMapper.toDto(entity.getVideo()))
                .conspectDocumentId(entity.getConspectDocument() == null
                        ? null : entity.getConspectDocument().getId())
                .conspectTitle(entity.getConspectDocument() == null
                        ? null : entity.getConspectDocument().getTitle())
                .testId(entity.getTest() == null ? null : entity.getTest().getId())
                .testTitle(entity.getTest() == null ? null : entity.getTest().getTitle())
                .passed(entity.getPassed())
                .build();
    }
}
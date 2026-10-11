package com.example.russianlanguagebot.mapper;

import com.example.russianlanguagebot.dto.SectionListItemDto;
import com.example.russianlanguagebot.dto.SectionRequestDto;
import com.example.russianlanguagebot.dto.SectionResponseDto;
import com.example.russianlanguagebot.dto.TopicListItemDto;
import com.example.russianlanguagebot.entity.ConspectDocument;
import com.example.russianlanguagebot.entity.Section;
import com.example.russianlanguagebot.entity.Video;
import com.example.russianlanguagebot.exception.NotFoundException;
import com.example.russianlanguagebot.repository.ConspectDocumentRepository;
import com.example.russianlanguagebot.repository.VideoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SectionMapper {

    private final TopicMapper topicMapper;
    private final VideoRepository videoRepository;
    private final ConspectDocumentRepository documentRepository;

    public SectionMapper(TopicMapper topicMapper,
                         VideoRepository videoRepository,
                         ConspectDocumentRepository documentRepository) {
        this.topicMapper = topicMapper;
        this.videoRepository = videoRepository;
        this.documentRepository = documentRepository;
    }

    public Section toEntity(SectionRequestDto dto) {
        Video video = null;
        if (dto.getVideoId() != null) {
            video = videoRepository.findById(dto.getVideoId())
                    .orElseThrow(() -> new NotFoundException("Видео", dto.getVideoId()));
        }

        ConspectDocument document = null;
        if (dto.getConspectDocumentId() != null) {
            document = documentRepository.findById(dto.getConspectDocumentId())
                    .orElseThrow(() -> new NotFoundException("Конспект", dto.getConspectDocumentId()));
        }

        return Section.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .video(video)
                .conspectDocument(document)
                .passed(false)
                .build();
    }

    public SectionResponseDto toDto(Section entity) {
        List<TopicListItemDto> topics = entity.getTopics() == null
                ? List.of()
                : entity.getTopics().stream()
                .map(topicMapper::toListItemDto)
                .toList();

        return SectionResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .videoId(entity.getVideo() == null ? null : entity.getVideo().getId())
                .videoTitle(entity.getVideo() == null ? null : entity.getVideo().getTitle())
                .conspectDocumentId(entity.getConspectDocument() == null ? null : entity.getConspectDocument().getId())
                .conspectTitle(entity.getConspectDocument() == null ? null : entity.getConspectDocument().getTitle())
                .passed(entity.getPassed())
                .topics(topics)
                .build();
    }

    public SectionListItemDto toListItemDto(Section entity) {
        int topicsCount = entity.getTopics() == null ? 0 : entity.getTopics().size();

        return SectionListItemDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .topicsCount(topicsCount)
                .passed(entity.getPassed())
                .build();
    }
}
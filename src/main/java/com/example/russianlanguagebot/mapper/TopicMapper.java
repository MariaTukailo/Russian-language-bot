package com.example.russianlanguagebot.mapper;

import com.example.russianlanguagebot.dto.TestResponseDto;
import com.example.russianlanguagebot.dto.TopicListItemDto;
import com.example.russianlanguagebot.dto.TopicRequestDto;
import com.example.russianlanguagebot.dto.TopicResponseDto;
import com.example.russianlanguagebot.entity.Section;
import com.example.russianlanguagebot.entity.Test;
import com.example.russianlanguagebot.entity.Topic;
import com.example.russianlanguagebot.entity.Video;
import com.example.russianlanguagebot.exception.NotFoundException;
import com.example.russianlanguagebot.repository.SectionRepository;
import com.example.russianlanguagebot.repository.VideoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TopicMapper {

    private final TestMapper testMapper;
    private final SectionRepository sectionRepository;
    private final VideoRepository videoRepository;

    public TopicMapper(TestMapper testMapper, SectionRepository sectionRepository, VideoRepository videoRepository) {
        this.testMapper = testMapper;
        this.sectionRepository = sectionRepository;
        this.videoRepository = videoRepository;
    }

    public Topic toEntity(TopicRequestDto dto) {
        return toEntity(dto, null);
    }

    public Topic toEntity(TopicRequestDto dto, Long sectionId) {
        List<Test> tests = dto.getTests() == null
                ? List.of()
                : dto.getTests().stream()
                .map(testMapper::toEntity)
                .toList();

        Section section = null;
        if (sectionId != null) {
            section = sectionRepository.findById(sectionId)
                    .orElseThrow(() -> new NotFoundException("Раздел", sectionId));
        }

        Video video = null;
        if (dto.getVideoId() != null) {
            video = videoRepository.findById(dto.getVideoId())
                    .orElseThrow(() -> new NotFoundException("Видео", dto.getVideoId()));
        }

        return Topic.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .section(section)
                .video(video)
                .conspectUrls(dto.getConspectUrls())
                .passed(false) // По умолчанию тема не пройдена
                .tests(tests)
                .build();
    }

    public TopicResponseDto toDto(Topic entity) {
        List<TestResponseDto> tests = entity.getTests() == null
                ? List.of()
                : entity.getTests().stream()
                .map(testMapper::toDto)
                .toList();

        return TopicResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .videoId(entity.getVideo() == null ? null : entity.getVideo().getId())
                .videoTitle(entity.getVideo() == null ? null : entity.getVideo().getTitle())
                .videoUrl(entity.getVideo() == null ? null : entity.getVideo().getUrl())
                .conspectUrls(entity.getConspectUrls())
                .passed(entity.getPassed()) // <--- ДОБАВЛЕНО
                .tests(tests)
                .build();
    }

    public TopicListItemDto toListItemDto(Topic entity) {
        int testsCount = entity.getTests() == null ? 0 : entity.getTests().size();

        return TopicListItemDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .testsCount(testsCount)
                .sectionId(entity.getSection() == null ? null : entity.getSection().getId())
                .sectionTitle(entity.getSection() == null ? null : entity.getSection().getTitle())
                .passed(entity.getPassed()) // <--- ДОБАВЛЕНО
                .build();
    }
}
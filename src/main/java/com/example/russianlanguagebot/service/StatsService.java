package com.example.russianlanguagebot.service;

import com.example.russianlanguagebot.dto.DashboardStatsDto;
import com.example.russianlanguagebot.repository.QuestionRepository;
import com.example.russianlanguagebot.repository.SectionRepository;
import com.example.russianlanguagebot.repository.TopicRepository;
import com.example.russianlanguagebot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsService {

    private final SectionRepository sectionRepository;
    private final TopicRepository topicRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public DashboardStatsDto getDashboardStats() {
        log.info("Запрос статистики для главной панели");
        return DashboardStatsDto.builder()
                .sectionsCount(sectionRepository.count())
                .topicsCount(topicRepository.count())
                .questionsCount(questionRepository.count())
                .usersCount(userRepository.count())
                .build();
    }
}
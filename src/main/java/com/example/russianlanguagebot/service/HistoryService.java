package com.example.russianlanguagebot.service;

import com.example.russianlanguagebot.dto.HistoryDetailsResponseDto;
import com.example.russianlanguagebot.dto.HistoryResponseDto;
import com.example.russianlanguagebot.entity.History;
import com.example.russianlanguagebot.entity.Test;
import com.example.russianlanguagebot.entity.User;
import com.example.russianlanguagebot.entity.UserAnswer;
import com.example.russianlanguagebot.exception.NotFoundException;
import com.example.russianlanguagebot.mapper.HistoryMapper;
import com.example.russianlanguagebot.repository.HistoryRepository;
import com.example.russianlanguagebot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final HistoryMapper historyMapper;

    public HistoryDetailsResponseDto findById(Long id) {
        return historyRepository.findById(id)
                .map(historyMapper::toDetailsDto)
                .orElseThrow(() -> {
                    log.warn("Запись истории id={} не найдена", id);
                    return new NotFoundException("История", id);
                });
    }

    public List<HistoryResponseDto> findByUserId(Long userId) {
        log.debug("Запрошена история тестов пользователя id={}", userId);
        return historyRepository.findAllByUserId(userId).stream()
                .map(historyMapper::toDto)
                .toList();
    }

    @Transactional
    public HistoryResponseDto create(Long userId, Test test, int correct, int total,
                                     List<UserAnswer> answers) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Пользователь id={} не найден при записи истории", userId);
                    return new NotFoundException("Пользователь", userId);
                });

        History history = History.builder()
                .user(user)
                .test(test)
                .correctAnswers(correct)
                .totalQuestions(total)
                .completedAt(LocalDateTime.now())
                .answers(answers)
                .build();


        answers.forEach(answer -> answer.setHistory(history));

        History saved = historyRepository.save(history);
        log.info("Записана попытка теста id={} пользователем id={}: {}/{}",
                test.getId(), userId, correct, total);
        return historyMapper.toDto(saved);
    }
}
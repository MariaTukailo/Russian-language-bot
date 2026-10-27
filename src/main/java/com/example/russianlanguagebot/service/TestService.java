package com.example.russianlanguagebot.service;

import com.example.russianlanguagebot.dto.AchievementResponseDto;
import com.example.russianlanguagebot.dto.AnswerDto;
import com.example.russianlanguagebot.dto.TestRequestDto;
import com.example.russianlanguagebot.dto.TestResponseDto;
import com.example.russianlanguagebot.dto.TestResultDto;
import com.example.russianlanguagebot.dto.TestSubmitDto;
import com.example.russianlanguagebot.entity.AnswerOption;
import com.example.russianlanguagebot.entity.Question;
import com.example.russianlanguagebot.entity.Test;
import com.example.russianlanguagebot.entity.User;
import com.example.russianlanguagebot.entity.UserAnswer;
import com.example.russianlanguagebot.exception.ConflictException;
import com.example.russianlanguagebot.exception.NotFoundException;
import com.example.russianlanguagebot.mapper.TestMapper;
import com.example.russianlanguagebot.repository.TestRepository;
import com.example.russianlanguagebot.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TestService {

    private final HistoryService historyService;
    private final TestRepository testRepository;
    private final TestMapper testMapper;
    private final UserRepository userRepository;
    private final AchievementService achievementService;

    public TestService(TestRepository testRepository,
                       TestMapper testMapper,
                       HistoryService historyService,
                       UserRepository userRepository,
                       AchievementService achievementService) {
        this.testRepository = testRepository;
        this.testMapper = testMapper;
        this.historyService = historyService;
        this.userRepository = userRepository;
        this.achievementService = achievementService;
    }

    @Transactional(readOnly = true)
    public List<TestResponseDto> findAll() {
        log.info("Запрос списка всех тестов");
        return testRepository.findAll().stream()
                .map(testMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public TestResponseDto findById(Long id) {
        log.info("Поиск теста по id={}", id);
        Test test = testRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Тест с id={} не найден", id);
                    return new NotFoundException("Тест", id);
                });
        return testMapper.toDto(test);
    }

    @Transactional
    public TestResponseDto save(TestRequestDto dto) {
        log.info("Сохранение теста: title={}", dto.getTitle());
        Test saved = testRepository.save(testMapper.toEntity(dto));
        log.info("Тест сохранён с id={}", saved.getId());
        return testMapper.toDto(saved);
    }

    @Transactional
    public TestResponseDto update(Long id, TestRequestDto dto) {
        log.info("Обновление теста id={}", id);

        if (!testRepository.existsById(id)) {
            log.warn("Тест с id={} не найден при обновлении", id);
            throw new NotFoundException("Тест", id);
        }

        Test toSave = testMapper.toEntity(dto);
        toSave.setId(id);
        Test updated = testRepository.save(toSave);
        log.info("Тест id={} обновлён", id);
        return testMapper.toDto(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Удаление теста id={}", id);
        if (!testRepository.existsById(id)) {
            log.warn("Нечего удалять — тест id={} не найден", id);
            throw new NotFoundException("Тест", id);
        }
        testRepository.deleteById(id);
        log.info("Тест id={} удалён", id);
    }

    @Transactional
    public TestResultDto submitTest(TestSubmitDto dto) {
        log.info("Сдача теста id={} пользователем id={}, ответов: {}",
                dto.getTestId(), dto.getUserId(),
                dto.getAnswers() == null ? 0 : dto.getAnswers().size());

        Test test = testRepository.findById(dto.getTestId())
                .orElseThrow(() -> new NotFoundException("Тест", dto.getTestId()));

        if (dto.getAnswers() == null) {
            throw new IllegalArgumentException("Список ответов не может быть null");
        }

        int total = test.getQuestions().size();

        if (dto.getAnswers().size() != total) {
            throw new IllegalArgumentException(
                    "Необходимо отправить ответ на каждый вопрос теста");
        }

        int correct = 0;

        List<UserAnswer> userAnswers = new ArrayList<>();
        Set<Long> answeredQuestions = new HashSet<>();

        for (AnswerDto answer : dto.getAnswers()) {

            Question question = test.getQuestions().stream()
                    .filter(q -> q.getId().equals(answer.getQuestionId()))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException(
                            "Вопрос", answer.getQuestionId()));

            if (!answeredQuestions.add(question.getId())) {
                throw new ConflictException(
                        "Повторный ответ на вопрос id=" + question.getId());
            }

            List<Long> selectedIds = answer.getSelectedOptionIds();

            if (selectedIds == null) {
                throw new IllegalArgumentException(
                        "Список выбранных вариантов не может быть null");
            }

            Set<Long> selectedIdSet = new HashSet<>(selectedIds);

            if (selectedIdSet.size() != selectedIds.size()) {
                throw new ConflictException("Вариант ответа указан повторно");
            }

            List<AnswerOption> options = question.getOptions();

            Set<Long> correctIds = options.stream()
                    .filter(AnswerOption::isCorrect)
                    .map(AnswerOption::getId)
                    .collect(Collectors.toSet());

            if (correctIds.isEmpty()) {
                throw new IllegalStateException(
                        "У вопроса id=" + question.getId()
                                + " нет правильных вариантов");
            }

            List<AnswerOption> selectedOptions = new ArrayList<>();

            for (Long selectedId : selectedIds) {
                AnswerOption selected = options.stream()
                        .filter(o -> o.getId().equals(selectedId))
                        .findFirst()
                        .orElseThrow(() -> new NotFoundException(
                                "Вариант ответа", selectedId));

                selectedOptions.add(selected);
            }

            boolean isCorrect = selectedIdSet.equals(correctIds);

            if (isCorrect) {
                correct++;
            }

            String correctText = options.stream()
                    .filter(AnswerOption::isCorrect)
                    .map(AnswerOption::getText)
                    .collect(Collectors.joining(", "));

            String selectedText = selectedOptions.stream()
                    .map(AnswerOption::getText)
                    .collect(Collectors.joining(", "));

            userAnswers.add(UserAnswer.builder()
                    .questionText(question.getText())
                    .selectedText(selectedText)
                    .correctText(correctText)
                    .correct(isCorrect)
                    .build());
        }

        historyService.create(
                dto.getUserId(),
                test,
                correct,
                total,
                userAnswers
        );

        // 1 правильный ответ = +1 очко
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь", dto.getUserId()));

        int currentCoins = user.getCoins() == null ? 0 : user.getCoins();
        user.setCoins(currentCoins + correct);
        userRepository.save(user);

        // Выдача достижений, порог которых достигнут
        List<AchievementResponseDto> newAchievements =
                achievementService.grantEligibleAchievements(user.getId());

        String message = "Правильных ответов: " + correct + " из " + total;

        log.info("Тест id={} сдан пользователем id={}. Правильных: {}/{}, начислено очков: {}, новых достижений: {}",
                test.getId(), dto.getUserId(), correct, total, correct, newAchievements.size());

        return TestResultDto.builder()
                .correctAnswers(correct)
                .totalQuestions(total)
                .message(message)
                .newAchievements(newAchievements)
                .build();
    }
}
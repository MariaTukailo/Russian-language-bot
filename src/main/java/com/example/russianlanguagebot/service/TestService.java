package com.example.russianlanguagebot.service;

import com.example.russianlanguagebot.dto.AnswerDto;
import com.example.russianlanguagebot.dto.TestRequestDto;
import com.example.russianlanguagebot.dto.TestResponseDto;
import com.example.russianlanguagebot.dto.TestResultDto;
import com.example.russianlanguagebot.dto.TestSubmitDto;
import com.example.russianlanguagebot.entity.AnswerOption;
import com.example.russianlanguagebot.entity.Question;
import com.example.russianlanguagebot.entity.Test;
import com.example.russianlanguagebot.exception.ConflictException;
import com.example.russianlanguagebot.exception.NotFoundException;
import com.example.russianlanguagebot.mapper.TestMapper;
import com.example.russianlanguagebot.repository.TestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class TestService {

    private final TestRepository testRepository;
    private final TestMapper testMapper;

    public TestService(TestRepository testRepository, TestMapper testMapper) {
        this.testRepository = testRepository;
        this.testMapper = testMapper;
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
        log.info("Сдача теста id={}, ответов: {}", dto.getTestId(), dto.getAnswers().size());

        Test test = testRepository.findById(dto.getTestId())
                .orElseThrow(() -> {
                    log.warn("Тест с id={} не найден при сдаче", dto.getTestId());
                    return new NotFoundException("Тест", dto.getTestId());
                });

        if (Boolean.TRUE.equals(test.getPassed())) {
            log.warn("Тест id={} уже был пройден", dto.getTestId());
            throw new ConflictException("Тест с id=" + dto.getTestId() + " уже был пройден");
        }

        int total = test.getQuestions().size();
        int correct = 0;

        for (AnswerDto answer : dto.getAnswers()) {
            Question question = test.getQuestions().stream()
                    .filter(q -> q.getId().equals(answer.getQuestionId()))
                    .findFirst()
                    .orElseThrow(() -> {
                        log.warn("Вопрос id={} не найден в тесте id={}",
                                answer.getQuestionId(), dto.getTestId());
                        return new NotFoundException("Вопрос", answer.getQuestionId());
                    });

            AnswerOption selected = question.getOptions().stream()
                    .filter(o -> o.getId().equals(answer.getSelectedOptionId()))
                    .findFirst()
                    .orElseThrow(() -> {
                        log.warn("Вариант id={} не найден в вопросе id={}",
                                answer.getSelectedOptionId(), answer.getQuestionId());
                        return new NotFoundException("Вариант ответа", answer.getSelectedOptionId());
                    });

            if (selected.isCorrect()) {
                correct++;
            }
        }

        boolean passed = correct >= (total / 2) + 1;

        test.setPassed(passed);
        testRepository.save(test);

        int xp = passed ? 50 : 0;
        int coins = passed ? 10 : 0;
        String message = "Правильных ответов: " + correct + " из " + total;

        log.info("Тест id={} сдан. Правильных: {}/{}, passed={}",
                test.getId(), correct, total, passed);

        return TestResultDto.builder()
                .passed(passed)
                .xpEarned(xp)
                .coinsEarned(coins)
                .message(message)
                .build();
    }
}
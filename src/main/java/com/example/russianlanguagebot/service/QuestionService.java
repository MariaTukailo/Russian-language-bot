package com.example.russianlanguagebot.service;

import com.example.russianlanguagebot.dto.QuestionRequestDto;
import com.example.russianlanguagebot.dto.QuestionResponseDto;
import com.example.russianlanguagebot.entity.Question;
import com.example.russianlanguagebot.exception.NotFoundException;
import com.example.russianlanguagebot.mapper.QuestionMapper;
import com.example.russianlanguagebot.repository.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    public QuestionService(QuestionRepository questionRepository,
                           QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
    }

    @Transactional(readOnly = true)
    public List<QuestionResponseDto> findAll() {
        log.info("Запрос списка всех вопросов");
        return questionRepository.findAll().stream()
                .map(questionMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public QuestionResponseDto findById(Long id) {
        log.info("Поиск вопроса по id={}", id);
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Вопрос с id={} не найден", id);
                    return new NotFoundException("Вопрос", id);
                });
        return questionMapper.toDto(question);
    }

    @Transactional
    public QuestionResponseDto save(QuestionRequestDto dto) {
        log.info("Сохранение вопроса: text={}", dto.getText());
        Question saved = questionRepository.save(questionMapper.toEntity(dto));
        log.info("Вопрос сохранён с id={}", saved.getId());
        return questionMapper.toDto(saved);
    }

    @Transactional
    public QuestionResponseDto update(Long id, QuestionRequestDto dto) {
        log.info("Обновление вопроса id={}", id);

        if (!questionRepository.existsById(id)) {
            log.warn("Вопрос с id={} не найден при обновлении", id);
            throw new NotFoundException("Вопрос", id);
        }

        Question toSave = questionMapper.toEntity(dto);
        toSave.setId(id);
        Question updated = questionRepository.save(toSave);
        log.info("Вопрос id={} обновлён", id);
        return questionMapper.toDto(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Удаление вопроса id={}", id);
        if (!questionRepository.existsById(id)) {
            log.warn("Нечего удалять — вопрос id={} не найден", id);
            throw new NotFoundException("Вопрос", id);
        }
        questionRepository.deleteById(id);
        log.info("Вопрос id={} удалён", id);
    }
}
package com.example.russianlanguagebot.service;

import com.example.russianlanguagebot.dto.AnswerOptionRequestDto;
import com.example.russianlanguagebot.dto.AnswerOptionResponseDto;
import com.example.russianlanguagebot.entity.AnswerOption;
import com.example.russianlanguagebot.exception.NotFoundException;
import com.example.russianlanguagebot.mapper.AnswerOptionMapper;
import com.example.russianlanguagebot.repository.AnswerOptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class AnswerOptionService {

    private final AnswerOptionRepository answerOptionRepository;
    private final AnswerOptionMapper answerOptionMapper;

    public AnswerOptionService(AnswerOptionRepository answerOptionRepository,
                               AnswerOptionMapper answerOptionMapper) {
        this.answerOptionRepository = answerOptionRepository;
        this.answerOptionMapper = answerOptionMapper;
    }

    @Transactional(readOnly = true)
    public List<AnswerOptionResponseDto> findAll() {
        log.info("Запрос списка всех вариантов ответа");
        return answerOptionRepository.findAll().stream()
                .map(answerOptionMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public AnswerOptionResponseDto findById(Long id) {
        log.info("Поиск варианта ответа по id={}", id);
        AnswerOption option = answerOptionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Вариант ответа с id={} не найден", id);
                    return new NotFoundException("Вариант ответа", id);
                });
        return answerOptionMapper.toDto(option);
    }

    @Transactional
    public AnswerOptionResponseDto save(AnswerOptionRequestDto dto) {
        log.info("Сохранение варианта ответа: text={}", dto.getText());
        AnswerOption saved = answerOptionRepository.save(answerOptionMapper.toEntity(dto));
        log.info("Вариант ответа сохранён с id={}", saved.getId());
        return answerOptionMapper.toDto(saved);
    }

    @Transactional
    public AnswerOptionResponseDto update(Long id, AnswerOptionRequestDto dto) {
        log.info("Обновление варианта ответа id={}", id);

        if (!answerOptionRepository.existsById(id)) {
            log.warn("Вариант ответа с id={} не найден при обновлении", id);
            throw new NotFoundException("Вариант ответа", id);
        }

        AnswerOption toSave = answerOptionMapper.toEntity(dto);
        toSave.setId(id);
        AnswerOption updated = answerOptionRepository.save(toSave);
        log.info("Вариант ответа id={} обновлён", id);
        return answerOptionMapper.toDto(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Удаление варианта ответа id={}", id);
        if (!answerOptionRepository.existsById(id)) {
            log.warn("Нечего удалять — вариант ответа id={} не найден", id);
            throw new NotFoundException("Вариант ответа", id);
        }
        answerOptionRepository.deleteById(id);
        log.info("Вариант ответа id={} удалён", id);
    }
}
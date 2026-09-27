package com.example.russianlanguagebot.controller;

import com.example.russianlanguagebot.dto.AnswerOptionRequestDto;
import com.example.russianlanguagebot.dto.AnswerOptionResponseDto;
import com.example.russianlanguagebot.service.AnswerOptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "Варианты ответа", description = "Управление вариантами ответа на вопросы тестов")
@RestController
@RequestMapping("/api/answer-options")
public class AnswerOptionController {

    private final AnswerOptionService answerOptionService;

    public AnswerOptionController(AnswerOptionService answerOptionService) {
        this.answerOptionService = answerOptionService;
    }

    @Operation(summary = "Получить все варианты ответа",
            description = "Возвращает список всех вариантов ответа (без флага correct)")
    @ApiResponse(responseCode = "200", description = "Список успешно получен")
    @GetMapping
    public List<AnswerOptionResponseDto> getAll() {
        return answerOptionService.findAll();
    }

    @Operation(summary = "Получить вариант ответа по id",
            description = "Возвращает вариант ответа по его идентификатору (без флага correct)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Вариант найден"),
            @ApiResponse(responseCode = "404", description = "Вариант не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AnswerOptionResponseDto> getById(
            @Parameter(description = "Идентификатор варианта ответа", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(answerOptionService.findById(id));
    }

    @Operation(summary = "Создать вариант ответа",
            description = "Создаёт новый вариант ответа. Поле correct принимается от админа и не возвращается наружу")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Вариант создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PostMapping
    public ResponseEntity<AnswerOptionResponseDto> create(
            @Valid @RequestBody AnswerOptionRequestDto dto) {
        return ResponseEntity.ok(answerOptionService.save(dto));
    }

    @Operation(summary = "Обновить вариант ответа",
            description = "Обновляет существующий вариант ответа по id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Вариант обновлён"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "404", description = "Вариант не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AnswerOptionResponseDto> update(
            @Parameter(description = "Идентификатор варианта ответа", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody AnswerOptionRequestDto dto) {
        return ResponseEntity.ok(answerOptionService.update(id, dto));
    }

    @Operation(summary = "Удалить вариант ответа", description = "Удаляет вариант ответа по id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Вариант удалён"),
            @ApiResponse(responseCode = "404", description = "Вариант не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Идентификатор варианта ответа", example = "1")
            @PathVariable Long id) {
        answerOptionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
package com.example.russianlanguagebot.controller;

import com.example.russianlanguagebot.dto.QuestionRequestDto;
import com.example.russianlanguagebot.dto.QuestionResponseDto;
import com.example.russianlanguagebot.service.QuestionService;
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
@Tag(name = "Вопросы", description = "Управление вопросами тестов")
@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Operation(summary = "Получить все вопросы",
            description = "Возвращает список всех вопросов с вариантами ответа (без флага correct)")
    @ApiResponse(responseCode = "200", description = "Список успешно получен")
    @GetMapping
    public List<QuestionResponseDto> getAll() {
        return questionService.findAll();
    }

    @Operation(summary = "Получить вопрос по id",
            description = "Возвращает вопрос по идентификатору с вариантами ответа (без флага correct)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Вопрос найден"),
            @ApiResponse(responseCode = "404", description = "Вопрос не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponseDto> getById(
            @Parameter(description = "Идентификатор вопроса", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(questionService.findById(id));
    }

    @Operation(summary = "Создать вопрос",
            description = "Создаёт вопрос с вариантами ответа. Поле correct принимается от админа")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Вопрос создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PostMapping
    public ResponseEntity<QuestionResponseDto> create(
            @Valid @RequestBody QuestionRequestDto dto) {
        return ResponseEntity.ok(questionService.save(dto));
    }

    @Operation(summary = "Обновить вопрос",
            description = "Обновляет существующий вопрос по id вместе с вариантами ответа")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Вопрос обновлён"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "404", description = "Вопрос не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponseDto> update(
            @Parameter(description = "Идентификатор вопроса", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody QuestionRequestDto dto) {
        return ResponseEntity.ok(questionService.update(id, dto));
    }

    @Operation(summary = "Удалить вопрос", description = "Удаляет вопрос вместе с вариантами ответа")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Вопрос удалён"),
            @ApiResponse(responseCode = "404", description = "Вопрос не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Идентификатор вопроса", example = "1")
            @PathVariable Long id) {
        questionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
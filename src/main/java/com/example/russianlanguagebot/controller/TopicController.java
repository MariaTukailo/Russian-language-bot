package com.example.russianlanguagebot.controller;

import com.example.russianlanguagebot.dto.TopicRequestDto;
import com.example.russianlanguagebot.dto.TopicResponseDto;
import com.example.russianlanguagebot.service.TopicService;
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
@Tag(name = "Темы", description = "Управление темами обучения")
@RestController
@RequestMapping("/api/topics")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @Operation(summary = "Получить все темы",
            description = "Возвращает список всех тем с вложенными тестами (без флага correct)")
    @ApiResponse(responseCode = "200", description = "Список успешно получен")
    @GetMapping
    public List<TopicResponseDto> getAll() {
        return topicService.findAll();
    }

    @Operation(summary = "Получить тему по id",
            description = "Возвращает тему с вложенными тестами, вопросами и вариантами (без флага correct)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Тема найдена"),
            @ApiResponse(responseCode = "404", description = "Тема не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TopicResponseDto> getById(
            @Parameter(description = "Идентификатор темы", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(topicService.findById(id));
    }

    @Operation(summary = "Создать тему",
            description = "Создаёт тему. Можно сразу передать вложенные тесты (с флагом correct), либо пустой список")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Тема создана"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PostMapping
    public ResponseEntity<TopicResponseDto> create(@Valid @RequestBody TopicRequestDto dto) {
        return ResponseEntity.ok(topicService.save(dto));
    }

    @Operation(summary = "Обновить тему",
            description = "Обновляет тему вместе с вложенными тестами (старые удаляются, новые вставляются)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Тема обновлена"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "404", description = "Тема не найдена")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TopicResponseDto> update(
            @Parameter(description = "Идентификатор темы", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody TopicRequestDto dto) {
        return ResponseEntity.ok(topicService.update(id, dto));
    }

    @Operation(summary = "Удалить тему",
            description = "Удаляет тему вместе со всеми вложенными тестами")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Тема удалена"),
            @ApiResponse(responseCode = "404", description = "Тема не найдена")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Идентификатор темы", example = "1")
            @PathVariable Long id) {
        topicService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
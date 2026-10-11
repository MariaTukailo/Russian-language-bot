package com.example.russianlanguagebot.controller;

import com.example.russianlanguagebot.dto.TopicListItemDto;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "Темы", description = "Управление темами внутри разделов")
@RestController
@RequestMapping("/api/topics")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @Operation(summary = "Получить список тем для админки",
            description = "Возвращает плоский список тем с количеством тестов")
    @ApiResponse(responseCode = "200", description = "Список успешно получен")
    @GetMapping("/list")
    public List<TopicListItemDto> getList() {
        return topicService.findAllForList();
    }

    @Operation(summary = "Получить темы раздела",
            description = "Возвращает список тем, принадлежащих указанному разделу")
    @ApiResponse(responseCode = "200", description = "Список успешно получен")
    @GetMapping("/by-section/{sectionId}")
    public List<TopicListItemDto> getBySection(
            @Parameter(description = "Идентификатор раздела", example = "1")
            @PathVariable Long sectionId) {
        return topicService.findBySectionId(sectionId);
    }

    @Operation(summary = "Получить все темы",
            description = "Возвращает список всех тем с вложенными тестами и вопросами")
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
            description = "Создаёт тему. sectionId — id раздела, к которому привязать (опционально)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Тема создана"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "404", description = "Раздел не найден")
    })
    @PostMapping
    public ResponseEntity<TopicResponseDto> create(
            @Parameter(description = "Идентификатор раздела", example = "1")
            @RequestParam(required = false) Long sectionId,
            @Valid @RequestBody TopicRequestDto dto) {
        return ResponseEntity.ok(topicService.save(dto, sectionId));
    }

    @Operation(summary = "Обновить тему",
            description = "Обновляет тему. sectionId — id раздела для перемещения (опционально)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Тема обновлена"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "404", description = "Тема или раздел не найдены")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TopicResponseDto> update(
            @Parameter(description = "Идентификатор темы", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Идентификатор раздела", example = "1")
            @RequestParam(required = false) Long sectionId,
            @Valid @RequestBody TopicRequestDto dto) {
        return ResponseEntity.ok(topicService.update(id, dto, sectionId));
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
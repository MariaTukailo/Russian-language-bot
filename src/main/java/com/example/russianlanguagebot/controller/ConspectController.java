package com.example.russianlanguagebot.controller;

import com.example.russianlanguagebot.dto.ConspectDocumentRequestDto;
import com.example.russianlanguagebot.dto.ConspectDocumentResponseDto;
import com.example.russianlanguagebot.service.ConspectService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "Конспекты", description = "Работа с конспектами: документы, страницы, темы")
@RestController
@RequestMapping("/api/conspects")
public class ConspectController {

    private final ConspectService conspectService;

    public ConspectController(ConspectService conspectService) {
        this.conspectService = conspectService;
    }

    @Operation(summary = "Получить все конспекты",
            description = "Возвращает список всех конспектов вместе со страницами и темами")
    @ApiResponse(responseCode = "200", description = "Список успешно получен")
    @GetMapping
    public List<ConspectDocumentResponseDto> getAll() {
        return conspectService.findAll();
    }

    @Operation(summary = "Получить конспект по id",
            description = "Возвращает документ со списком страниц и разметкой тем")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Конспект найден"),
            @ApiResponse(responseCode = "404", description = "Конспект не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ConspectDocumentResponseDto> getById(
            @Parameter(description = "Идентификатор конспекта", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(conspectService.findById(id));
    }

    @Operation(summary = "Создать конспект",
            description = "Создаёт документ конспекта. Страницы и темы добавляются отдельно при импорте PDF")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Конспект создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PostMapping
    public ResponseEntity<ConspectDocumentResponseDto> create(
            @Valid @RequestBody ConspectDocumentRequestDto dto) {
        return ResponseEntity.ok(conspectService.save(dto));
    }

    @Operation(summary = "Удалить конспект",
            description = "Удаляет документ со всеми его страницами и разметкой тем")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Конспект удалён"),
            @ApiResponse(responseCode = "404", description = "Конспект не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Идентификатор конспекта", example = "1")
            @PathVariable Long id) {
        conspectService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Удалить тему конспекта",
            description = "Удаляет привязку темы к диапазону страниц. Сам документ не трогается")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Тема удалена"),
            @ApiResponse(responseCode = "404", description = "Тема конспекта не найдена")
    })
    @DeleteMapping("/topics/{id}")
    public ResponseEntity<Void> deleteTopic(
            @Parameter(description = "Идентификатор темы конспекта", example = "1")
            @PathVariable Long id) {
        conspectService.deleteTopic(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить страницы темы конспекта",
            description = "Возвращает список URL картинок-страниц для указанной темы конспекта")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список страниц получен"),
            @ApiResponse(responseCode = "404", description = "Тема конспекта не найдена")
    })
    @GetMapping("/topics/{conspectTopicId}/pages")
    public ResponseEntity<List<String>> getTopicPages(
            @Parameter(description = "Идентификатор темы конспекта (ConspectTopic)", example = "1")
            @PathVariable Long conspectTopicId) {
        return ResponseEntity.ok(conspectService.getPagesForTopic(conspectTopicId));
    }
}
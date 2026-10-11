package com.example.russianlanguagebot.controller;

import com.example.russianlanguagebot.dto.SectionListItemDto;
import com.example.russianlanguagebot.dto.SectionRequestDto;
import com.example.russianlanguagebot.dto.SectionResponseDto;
import com.example.russianlanguagebot.service.SectionService;
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
@Tag(name = "Разделы", description = "Управление разделами курса")
@RestController
@RequestMapping("/api/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @Operation(summary = "Получить список разделов для админки",
            description = "Возвращает плоский список разделов с количеством тем")
    @ApiResponse(responseCode = "200", description = "Список успешно получен")
    @GetMapping("/list")
    public List<SectionListItemDto> getList() {
        return sectionService.findAllForList();
    }

    @Operation(summary = "Получить все разделы",
            description = "Возвращает список всех разделов с вложенными темами")
    @ApiResponse(responseCode = "200", description = "Список успешно получен")
    @GetMapping
    public List<SectionResponseDto> getAll() {
        return sectionService.findAll();
    }

    @Operation(summary = "Получить раздел по id",
            description = "Возвращает раздел с вложенными темами")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Раздел найден"),
            @ApiResponse(responseCode = "404", description = "Раздел не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SectionResponseDto> getById(
            @Parameter(description = "Идентификатор раздела", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(sectionService.findById(id));
    }

    @Operation(summary = "Создать раздел", description = "Создаёт новый раздел курса")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Раздел создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PostMapping
    public ResponseEntity<SectionResponseDto> create(@Valid @RequestBody SectionRequestDto dto) {
        return ResponseEntity.ok(sectionService.save(dto));
    }

    @Operation(summary = "Обновить раздел", description = "Обновляет раздел по id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Раздел обновлён"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "404", description = "Раздел не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SectionResponseDto> update(
            @Parameter(description = "Идентификатор раздела", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody SectionRequestDto dto) {
        return ResponseEntity.ok(sectionService.update(id, dto));
    }

    @Operation(summary = "Отметить раздел пройденным")
    @PutMapping("/{id}/pass")
    public ResponseEntity<SectionResponseDto> markPassed(@PathVariable Long id) {
        return ResponseEntity.ok(sectionService.markPassed(id));
    }

    @Operation(summary = "Удалить раздел",
            description = "Удаляет раздел вместе со всеми вложенными темами")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Раздел удалён"),
            @ApiResponse(responseCode = "404", description = "Раздел не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Идентификатор раздела", example = "1")
            @PathVariable Long id) {
        sectionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
package com.example.russianlanguagebot.controller;

import com.example.russianlanguagebot.dto.LearningMapRequestDto;
import com.example.russianlanguagebot.dto.LearningMapResponseDto;
import com.example.russianlanguagebot.dto.MapSectionRequestDto;
import com.example.russianlanguagebot.dto.MapSectionResponseDto;
import com.example.russianlanguagebot.service.MapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "Карта обучения", description = "Управление картой и её разделами")
@RestController
@RequestMapping("/api/maps")
public class MapController {

    private final MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @Operation(summary = "Получить все карты",
            description = "Возвращает список всех карт с разделами")
    @ApiResponse(responseCode = "200", description = "Список успешно получен")
    @GetMapping
    public List<LearningMapResponseDto> getAll() {
        return mapService.findAll();
    }

    @Operation(summary = "Получить карту по id",
            description = "Возвращает карту с разделами: видео, конспект, тест")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Карта найдена"),
            @ApiResponse(responseCode = "404", description = "Карта не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LearningMapResponseDto> getById(
            @Parameter(description = "Идентификатор карты", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(mapService.findById(id));
    }

    @Operation(summary = "Создать карту",
            description = "Создаёт карту сразу со списком разделов. Список может быть пустым")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Карта создана"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "404", description = "Одна из связанных сущностей не найдена")
    })
    @PostMapping
    public ResponseEntity<LearningMapResponseDto> create(
            @Valid @RequestBody LearningMapRequestDto dto) {
        return ResponseEntity.ok(mapService.save(dto));
    }

    @Operation(summary = "Создать раздел карты",
            description = "Создаёт раздел карты, связывая видео, конспект и тест по их id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Раздел создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "404", description = "Видео, конспект или тест не найдены")
    })
    @PostMapping("/sections")
    public ResponseEntity<MapSectionResponseDto> createSection(
            @Valid @RequestBody MapSectionRequestDto dto) {
        return ResponseEntity.ok(mapService.saveSection(dto));
    }

    @Operation(summary = "Отметить раздел пройденным",
            description = "Устанавливает флаг passed=true у раздела карты")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Раздел отмечен пройденным"),
            @ApiResponse(responseCode = "404", description = "Раздел карты не найден")
    })
    @PutMapping("/sections/{id}/pass")
    public ResponseEntity<MapSectionResponseDto> markPassed(
            @Parameter(description = "Идентификатор раздела карты", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(mapService.markSectionPassed(id));
    }
}
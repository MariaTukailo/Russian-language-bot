package com.example.russianlanguagebot.controller;

import com.example.russianlanguagebot.dto.LearningMapRequestDto;
import com.example.russianlanguagebot.dto.LearningMapResponseDto;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "Карта обучения", description = "Управление картой обучения")
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
            description = "Возвращает карту со всеми разделами и темами")
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
            description = "Создаёт единственную карту. Повторный вызов вернёт 409. Список разделов может быть пустым")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Карта создана"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "409", description = "Карта уже существует")
    })
    @PostMapping
    public ResponseEntity<LearningMapResponseDto> create(
            @Valid @RequestBody LearningMapRequestDto dto) {
        return ResponseEntity.ok(mapService.save(dto));
    }
}
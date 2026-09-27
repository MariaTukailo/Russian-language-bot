package com.example.russianlanguagebot.controller;

import com.example.russianlanguagebot.dto.VideoDto;
import com.example.russianlanguagebot.service.VideoService;
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
@Tag(name = "Видео", description = "Управление видеоматериалами")
@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @Operation(summary = "Получить все видео", description = "Возвращает список всех видеоматериалов")
    @ApiResponse(responseCode = "200", description = "Список успешно получен")
    @GetMapping
    public List<VideoDto> getAll() {
        return videoService.findAll();
    }

    @Operation(summary = "Получить видео по id", description = "Возвращает видео по его идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Видео найдено"),
            @ApiResponse(responseCode = "404", description = "Видео не найдено")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VideoDto> getById(
            @Parameter(description = "Идентификатор видео", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(videoService.findById(id));
    }

    @Operation(summary = "Создать видео", description = "Создаёт новый видеоматериал")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Видео создано"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PostMapping
    public ResponseEntity<VideoDto> create(@Valid @RequestBody VideoDto dto) {
        return ResponseEntity.ok(videoService.save(dto));
    }

    @Operation(summary = "Обновить видео", description = "Обновляет существующее видео по id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Видео обновлено"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "404", description = "Видео не найдено")
    })
    @PutMapping("/{id}")
    public ResponseEntity<VideoDto> update(
            @Parameter(description = "Идентификатор видео", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody VideoDto dto) {
        return ResponseEntity.ok(videoService.update(id, dto));
    }

    @Operation(summary = "Удалить видео", description = "Удаляет видео по id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Видео удалено"),
            @ApiResponse(responseCode = "404", description = "Видео не найдено")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Идентификатор видео", example = "1")
            @PathVariable Long id) {
        videoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
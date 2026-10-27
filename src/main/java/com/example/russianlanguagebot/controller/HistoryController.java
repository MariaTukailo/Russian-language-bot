package com.example.russianlanguagebot.controller;

import com.example.russianlanguagebot.dto.HistoryDetailsResponseDto;
import com.example.russianlanguagebot.dto.HistoryResponseDto;
import com.example.russianlanguagebot.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "История", description = "История прохождения тестов")
@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;


    @Operation(summary = "Получить попытку по id",
            description = "Возвращает попытку со счётом и разбором каждого ответа")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Попытка найдена"),
            @ApiResponse(responseCode = "404", description = "Попытка не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<HistoryDetailsResponseDto> findById(
            @Parameter(description = "Идентификатор записи истории", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(historyService.findById(id));
    }


    @Operation(summary = "Получить историю пользователя",
            description = "Возвращает список попыток пользователя, новые сверху")
    @ApiResponse(responseCode = "200", description = "Список успешно получен")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HistoryResponseDto>> findByUserId(
            @Parameter(description = "Идентификатор пользователя", example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(historyService.findByUserId(userId));
    }
}
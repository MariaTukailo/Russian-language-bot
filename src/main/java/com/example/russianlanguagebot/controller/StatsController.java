package com.example.russianlanguagebot.controller;

import com.example.russianlanguagebot.dto.DashboardStatsDto;
import com.example.russianlanguagebot.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@Tag(name = "Статистика", description = "Данные для главной панели")
public class StatsController {

    private final StatsService statsService;

    @Operation(summary = "Получить общую статистику")
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsDto> getDashboardStats() {
        return ResponseEntity.ok(statsService.getDashboardStats());
    }
}
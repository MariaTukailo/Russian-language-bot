package com.example.russianlanguagebot.controller;

import com.example.russianlanguagebot.dto.HistoryResponseDto;
import com.example.russianlanguagebot.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;


    @GetMapping("/{id}")
    public ResponseEntity<HistoryResponseDto> findById(
            @PathVariable Long id) {
        return ResponseEntity.ok(historyService.findById(id));
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HistoryResponseDto>> findByUserId(
            @PathVariable Long userId) {
        return ResponseEntity.ok(
                historyService.findByUserId(userId)
        );
    }

    // Проверить, проходил ли пользователь тест
    @GetMapping("/user/{userId}/test/{testId}/passed")
    public ResponseEntity<Boolean> isTestPassed(
            @PathVariable Long userId,
            @PathVariable Long testId) {
        return ResponseEntity.ok(
                historyService.isTestPassed(userId, testId)
        );
    }
}


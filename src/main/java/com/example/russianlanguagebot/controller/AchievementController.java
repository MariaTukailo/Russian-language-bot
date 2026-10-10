package com.example.russianlanguagebot.controller;

import com.example.russianlanguagebot.dto.AchievementRequestDto;
import com.example.russianlanguagebot.dto.AchievementResponseDto;
import com.example.russianlanguagebot.service.AchievementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;


    @GetMapping
    public ResponseEntity<List<AchievementResponseDto>> findAll() {
        return ResponseEntity.ok(achievementService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<AchievementResponseDto> findById(
            @PathVariable Long id) {
        return ResponseEntity.ok(achievementService.findById(id));
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AchievementResponseDto>> findByUserId(
            @PathVariable Long userId) {
        return ResponseEntity.ok(
                achievementService.findByUserId(userId)
        );
    }


    @PostMapping
    public ResponseEntity<AchievementResponseDto> create(
            @Valid @RequestBody AchievementRequestDto dto) {
        AchievementResponseDto created = achievementService.create(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        achievementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


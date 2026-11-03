package com.example.russianlanguagebot.controller;

import com.example.russianlanguagebot.dto.AuthResponseDto;
import com.example.russianlanguagebot.dto.TelegramAuthRequestDto;
import com.example.russianlanguagebot.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Авторизация", description = "Вход и регистрация через Telegram")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/telegram")
    @Operation(
            summary = "Вход/регистрация через Telegram",
            description = "Если пользователь с таким telegramId есть — возвращает его (вход). Если нет — создаёт нового с ролью USER (регистрация)."
    )
    public ResponseEntity<AuthResponseDto> authenticateTelegram(
            @Valid @RequestBody TelegramAuthRequestDto request) {
        return ResponseEntity.ok(authService.authenticateTelegram(request));
    }
}
package com.example.russianlanguagebot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на авторизацию/регистрацию через Telegram")
public class TelegramAuthRequestDto {

    @Schema(description = "ID пользователя в Telegram", example = "123456789")
    @NotNull(message = "Telegram ID обязателен")
    private Long telegramId;

    @Schema(description = "Username в Telegram", example = "ivan_petrov")
    @NotBlank(message = "Username обязателен")
    private String username;
}
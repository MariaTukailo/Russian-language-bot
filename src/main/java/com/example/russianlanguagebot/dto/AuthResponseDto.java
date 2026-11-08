package com.example.russianlanguagebot.dto;

import com.example.russianlanguagebot.entity.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ после авторизации")
public class AuthResponseDto {

    @Schema(description = "Данные пользователя")
    private UserDto user;


}
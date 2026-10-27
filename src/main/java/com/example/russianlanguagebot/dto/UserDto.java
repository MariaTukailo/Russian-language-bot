package com.example.russianlanguagebot.dto;

import com.example.russianlanguagebot.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    private Long telegramId;

    private String username;

    private Role role;

    private Integer coins;

    private LocalDateTime createdAt;
}
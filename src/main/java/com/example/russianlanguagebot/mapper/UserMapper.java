package com.example.russianlanguagebot.mapper;

import com.example.russianlanguagebot.dto.UserCreateDto;
import com.example.russianlanguagebot.dto.UserDto;
import com.example.russianlanguagebot.dto.UserUpdateDto;
import com.example.russianlanguagebot.entity.User;
import com.example.russianlanguagebot.entity.enums.Role;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                .id(user.getId())
                .telegramId(user.getTelegramId())
                .username(user.getUsername())
                .role(user.getRole())
                .coins(user.getCoins())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public User toEntity(UserCreateDto dto) {
        if (dto == null) {
            return null;
        }
        return User.builder()
                .username(dto.getUsername())
                .role(Role.USER)
                .coins(0)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void updateEntity(User user, UserUpdateDto dto) {
        if (dto == null) return;
        if (dto.getUsername() != null) {
            user.setUsername(dto.getUsername());
        }
        if (dto.getCoins() != null) {
            user.setCoins(dto.getCoins());
        }
    }
}
package com.example.russianlanguagebot.service;

import com.example.russianlanguagebot.dto.AuthResponseDto;
import com.example.russianlanguagebot.dto.TelegramAuthRequestDto;
import com.example.russianlanguagebot.entity.User;
import com.example.russianlanguagebot.entity.enums.Role;
import com.example.russianlanguagebot.mapper.UserMapper;
import com.example.russianlanguagebot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public AuthResponseDto authenticateTelegram(TelegramAuthRequestDto request) {
        log.info("Попытка авторизации через Telegram: tgId={}", request.getTelegramId());

        return userRepository.findByTelegramId(request.getTelegramId())
                .map(existingUser -> {
                    log.info("Успешный вход: userId={}, tgId={}", existingUser.getId(), request.getTelegramId());

                    if (request.getUsername() != null && !request.getUsername().equals(existingUser.getUsername())) {
                        existingUser.setUsername(request.getUsername());
                    }

                    return AuthResponseDto.builder()
                            .user(userMapper.toDto(existingUser))
                            .build();
                })
                .orElseGet(() -> {
                    log.info("Регистрация нового пользователя: tgId={}", request.getTelegramId());

                    User newUser = User.builder()
                            .telegramId(request.getTelegramId())
                            .username(request.getUsername())
                            .role(Role.USER)
                            .coins(0)
                            .createdAt(LocalDateTime.now())
                            .achievements(new ArrayList<>())
                            .histories(new ArrayList<>())
                            .build();

                    User saved = userRepository.save(newUser);
                    log.info("Пользователь создан: userId={}", saved.getId());

                    return AuthResponseDto.builder()
                            .user(userMapper.toDto(saved))
                            .build();
                });
    }
}
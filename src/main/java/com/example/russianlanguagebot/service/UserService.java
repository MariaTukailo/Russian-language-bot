package com.example.russianlanguagebot.service;

import com.example.russianlanguagebot.dto.UserCreateDto;
import com.example.russianlanguagebot.dto.UserDto;
import com.example.russianlanguagebot.dto.UserUpdateDto;
import com.example.russianlanguagebot.entity.User;
import com.example.russianlanguagebot.mapper.UserMapper;
import com.example.russianlanguagebot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserDto create(UserCreateDto dto) {
        User user = userMapper.toEntity(dto);
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Transactional
    public UserDto update(Long id, UserUpdateDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        userMapper.updateEntity(user, dto);
        return userMapper.toDto(user); // грязная проверка Hibernate сохранит сам
    }

    @Transactional(readOnly = true)
    public UserDto getById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }
}
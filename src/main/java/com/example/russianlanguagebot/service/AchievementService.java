package com.example.russianlanguagebot.service;

import com.example.russianlanguagebot.dto.AchievementRequestDto;
import com.example.russianlanguagebot.dto.AchievementResponseDto;
import com.example.russianlanguagebot.entity.Achievement;
import com.example.russianlanguagebot.entity.User;
import com.example.russianlanguagebot.mapper.AchievementMapper;
import com.example.russianlanguagebot.repository.AchievementRepository;
import com.example.russianlanguagebot.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserRepository userRepository;
    private final AchievementMapper achievementMapper;

    public List<AchievementResponseDto> findAll() {
        log.debug("Запрошен список всех достижений");
        return achievementRepository.findAll().stream()
                .map(achievementMapper::toDto)
                .toList();
    }

    public AchievementResponseDto findById(Long id) {
        return achievementRepository.findById(id)
                .map(achievementMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("Достижение id={} не найдено", id);
                    return new EntityNotFoundException("Достижение не найдено: " + id);
                });
    }

    public List<AchievementResponseDto> findByUserId(Long userId) {
        log.debug("Поиск достижений пользователя id={}", userId);
        // ИСПРАВЛЕНО ТУТ: findAllByUsersId вместо findAllByUserId
        return achievementRepository.findAllByUsersId(userId).stream()
                .map(achievementMapper::toDto)
                .toList();
    }

    @Transactional
    public AchievementResponseDto create(AchievementRequestDto dto) {
        Achievement saved = achievementRepository.save(achievementMapper.toEntity(dto));
        log.info("Создано достижение id={}, название='{}'", saved.getId(), saved.getName());
        return achievementMapper.toDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Достижение id={} не найдено", id);
                    return new EntityNotFoundException("Достижение не найдено: " + id);
                });

        achievement.getUsers().forEach(user ->
                user.getAchievements().removeIf(a -> a.getId().equals(id)));

        achievementRepository.delete(achievement);
        log.info("Достижение id={} удалено, отвязано от пользователей: {}",
                id, achievement.getUsers().size());
    }

    @Transactional
    public List<AchievementResponseDto> grantEligibleAchievements(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Пользователь id={} не найден при выдаче достижений", userId);
                    return new EntityNotFoundException("Пользователь не найден: " + userId);
                });

        if (user.getCoins() == null) {
            return List.of();
        }

        List<Achievement> eligible =
                achievementRepository.findByNumberPointsLessThanEqual(user.getCoins());

        Set<Long> currentIds = user.getAchievements().stream()
                .map(Achievement::getId)
                .collect(Collectors.toSet());

        List<Achievement> newOnes = eligible.stream()
                .filter(a -> !currentIds.contains(a.getId()))
                .toList();

        if (newOnes.isEmpty()) {
            return List.of();
        }

        user.getAchievements().addAll(newOnes);
        userRepository.save(user);

        log.info("Пользователю id={} выданы достижения: {}",
                userId, newOnes.stream().map(Achievement::getName).toList());

        return newOnes.stream()
                .map(achievementMapper::toDto)
                .toList();
    }
}
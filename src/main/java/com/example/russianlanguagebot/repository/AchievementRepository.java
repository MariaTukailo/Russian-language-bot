package com.example.russianlanguagebot.repository;

import com.example.russianlanguagebot.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {


    List<Achievement> findAllByUsersId(Long userId);

    List<Achievement> findByNumberPointsLessThanEqual(Integer points);
}
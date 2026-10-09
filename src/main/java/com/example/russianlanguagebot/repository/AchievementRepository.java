package com.example.russianlanguagebot.repository;

import com.example.russianlanguagebot.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementRepository extends JpaRepository<Achievement,Long> {
}

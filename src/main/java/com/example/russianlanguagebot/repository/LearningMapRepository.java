package com.example.russianlanguagebot.repository;

import com.example.russianlanguagebot.entity.LearningMap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningMapRepository extends JpaRepository<LearningMap, Long> {
}
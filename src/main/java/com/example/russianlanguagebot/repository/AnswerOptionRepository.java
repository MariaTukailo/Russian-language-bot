package com.example.russianlanguagebot.repository;

import com.example.russianlanguagebot.entity.AnswerOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerOptionRepository extends JpaRepository<AnswerOption, Long> {
}
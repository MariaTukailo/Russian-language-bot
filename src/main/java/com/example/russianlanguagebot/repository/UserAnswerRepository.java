package com.example.russianlanguagebot.repository;

import com.example.russianlanguagebot.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAnswerRepository extends JpaRepository<UserAnswer,Long> {
}

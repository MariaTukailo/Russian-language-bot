package com.example.russianlanguagebot.repository;

import com.example.russianlanguagebot.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
}
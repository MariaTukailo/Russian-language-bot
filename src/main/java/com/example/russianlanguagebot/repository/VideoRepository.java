package com.example.russianlanguagebot.repository;

import com.example.russianlanguagebot.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
}
package com.example.russianlanguagebot.repository;

import com.example.russianlanguagebot.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {
}
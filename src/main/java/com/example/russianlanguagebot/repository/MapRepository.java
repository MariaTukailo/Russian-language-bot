package com.example.russianlanguagebot.repository;

import com.example.russianlanguagebot.entity.StudyMap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MapRepository extends JpaRepository<StudyMap,Long> {
}

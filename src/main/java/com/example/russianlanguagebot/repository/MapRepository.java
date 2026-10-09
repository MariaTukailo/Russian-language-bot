package com.example.russianlanguagebot.repository;

import com.example.russianlanguagebot.mapper.MapMapper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MapRepository extends JpaRepository<MapMapper,Long> {
}

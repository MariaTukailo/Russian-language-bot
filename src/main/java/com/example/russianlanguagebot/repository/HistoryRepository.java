package com.example.russianlanguagebot.repository;

import com.example.russianlanguagebot.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  HistoryRepository extends JpaRepository<History,Long> {
}

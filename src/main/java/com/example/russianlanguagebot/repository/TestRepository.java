package com.example.russianlanguagebot.repository;

import com.example.russianlanguagebot.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
}
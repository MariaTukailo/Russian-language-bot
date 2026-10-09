package com.example.russianlanguagebot.repository;

import com.example.russianlanguagebot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}

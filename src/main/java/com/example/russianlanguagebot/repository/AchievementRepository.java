package com.example.russianlanguagebot.repository;

import com.example.russianlanguagebot.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AchievementRepository extends JpaRepository<Achievement,Long> {
    List<Achievement> findAllByUsersId(Long userId);

    @Query("select a from Achievement a join a.users u where u.id = :userId")
    List<Achievement> findAllByUserId(@Param("userId") Long userId);
}

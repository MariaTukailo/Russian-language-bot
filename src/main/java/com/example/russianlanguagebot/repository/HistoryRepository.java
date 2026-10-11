package com.example.russianlanguagebot.repository;

import com.example.russianlanguagebot.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {

    @Query("select h from History h join fetch h.test where h.user.id = :userId order by h.completedAt desc")
    List<History> findAllByUserId(@Param("userId") Long userId);

    boolean existsByUserIdAndTestId(Long userId, Long testId);
}
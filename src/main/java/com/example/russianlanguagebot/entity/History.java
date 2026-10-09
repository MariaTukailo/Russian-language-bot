package com.example.russianlanguagebot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "histories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    @Column(name="correct_answers")
    private Integer correctAnswersCount;

    @Column(name="total_questions")
    private Integer totalQuestionsCount;

    @Column(name="passed")
    private Boolean passed;

    @Column(name="coins")
    private Integer coinsEarned;

    @Column(name="complete_at")
    private LocalDateTime completedAt;
}
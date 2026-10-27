
package com.example.russianlanguagebot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_answers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "history_id")
    private History history;

    @Column(name = "question_text", columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "selected_text", columnDefinition = "TEXT")
    private String selectedText;

    @Column(name = "correct_text", columnDefinition = "TEXT")
    private String correctText;

    @Column(name = "correct")
    private Boolean correct;
}
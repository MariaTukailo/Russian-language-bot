package com.example.russianlanguagebot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "topics")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Boolean passed;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    // Связь OneToOne (Одно видео привязано только к одной теме)
    @OneToOne
    @JoinColumn(name = "video_id")
    private Video video;

    // Список ссылок на картинки конспекта.
    // @ElementCollection создаст отдельную таблицу topic_conspect_urls (topic_id, conspect_url)
    @ElementCollection
    @CollectionTable(name = "topic_conspect_urls", joinColumns = @JoinColumn(name = "topic_id"))
    @Column(name = "conspect_url", columnDefinition = "TEXT")
    private List<String> conspectUrls;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Test> tests;
}
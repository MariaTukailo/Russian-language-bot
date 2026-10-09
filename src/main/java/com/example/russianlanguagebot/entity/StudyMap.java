package com.example.russianlanguagebot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="maps")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StudyMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinColumn(name = "map_id")
    private List<MapSection> sections;
}

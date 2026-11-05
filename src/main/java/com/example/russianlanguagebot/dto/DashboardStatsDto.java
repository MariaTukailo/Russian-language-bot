package com.example.russianlanguagebot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDto {
    private long sectionsCount;
    private long topicsCount;
    private long questionsCount;
    private long usersCount;
}
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
public class TestResultDto {

    private boolean passed;

    private int xpEarned;

    private int coinsEarned;

    private String message;
}
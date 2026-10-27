
package com.example.russianlanguagebot.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnswerDto {

    private Long questionId;

    private List<Long> selectedOptionIds;
}

package com.example.russianlanguagebot.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TestSubmitDto {

    private Long testId;

    private Long userId;

    private List<AnswerDto> answers;
}
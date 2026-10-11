package com.example.russianlanguagebot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class SectionRequestDto {

    @NotBlank(message = "Название раздела обязательно")
    @Size(max = 255, message = "Название не должно превышать 255 символов")
    private String title;

    @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
    private String description;

    private Long videoId;

    private Long conspectDocumentId;
}
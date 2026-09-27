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
public class VideoDto {

    private Long id;

    @NotBlank(message = "Название видео обязательно")
    @Size(max = 255, message = "Название не должно превышать 255 символов")
    private String title;

    @Size(max = 2000, message = "Описание не должно превышать 2000 символов")
    private String description;

    @NotBlank(message = "Ссылка на видео обязательна")
    @Size(max = 500, message = "Ссылка не должна превышать 500 символов")
    private String url;
}
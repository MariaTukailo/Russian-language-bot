package com.example.russianlanguagebot.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ConspectDocumentRequestDto {

    @NotBlank(message = "Название документа обязательно")
    @Size(max = 255, message = "Название не должно превышать 255 символов")
    private String title;

    @NotBlank(message = "Имя файла обязательно")
    @Size(max = 255, message = "Имя файла не должно превышать 255 символов")
    private String originalFileName;

    @NotNull(message = "Количество страниц обязательно")
    @Min(value = 1, message = "В документе должна быть хотя бы одна страница")
    private Integer totalPages;
}
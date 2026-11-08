package com.example.russianlanguagebot.service.image;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@ConditionalOnProperty(name = "image.upload.provider", havingValue = "local", matchIfMissing = true)
public class LocalUploader implements ImageUploader {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public String upload(MultipartFile file) {
        try {
            Path uploadPath = Paths.get(uploadDir, "images");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalName = file.getOriginalFilename();
            String extension = originalName != null && originalName.contains(".")
                    ? originalName.substring(originalName.lastIndexOf("."))
                    : ".png";
            String fileName = UUID.randomUUID().toString() + extension;

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            String fileUrl = "/files/images/" + fileName;
            log.info("Файл сохранен локально: {}", fileUrl);

            return fileUrl;

        } catch (IOException e) {
            log.error("Ошибка локальной загрузки файла", e);
            throw new RuntimeException("Не удалось сохранить файл локально", e);
        }
    }
}
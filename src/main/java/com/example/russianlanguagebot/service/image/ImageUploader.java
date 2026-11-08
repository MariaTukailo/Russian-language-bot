package com.example.russianlanguagebot.service.image;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {
    String upload(MultipartFile file);
}
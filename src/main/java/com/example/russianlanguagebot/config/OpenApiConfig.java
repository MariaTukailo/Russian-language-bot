package com.example.russianlanguagebot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Russian Language Bot API")
                        .version("1.0.0")
                        .description("API для Telegram-бота и веб-приложения по изучению русского языка")
                        .contact(new Contact()
                                .name("Команда проекта")
                                .email("team@example.com")));
    }
}
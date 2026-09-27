package com.example.russianlanguagebot.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String entityName, Long id) {
        super(entityName + " с id=" + id + " не найден");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
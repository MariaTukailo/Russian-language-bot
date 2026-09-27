package com.example.russianlanguagebot.controller;

import com.example.russianlanguagebot.dto.TestRequestDto;
import com.example.russianlanguagebot.dto.TestResponseDto;
import com.example.russianlanguagebot.dto.TestResultDto;
import com.example.russianlanguagebot.dto.TestSubmitDto;
import com.example.russianlanguagebot.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "Тесты", description = "Управление тестами и их прохождение")
@RestController
@RequestMapping("/api/tests")
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @Operation(summary = "Получить все тесты",
            description = "Возвращает список всех тестов (без флага correct в вариантах)")
    @ApiResponse(responseCode = "200", description = "Список успешно получен")
    @GetMapping
    public List<TestResponseDto> getAll() {
        return testService.findAll();
    }

    @Operation(summary = "Получить тест по id",
            description = "Возвращает тест с вопросами и вариантами (без флага correct)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Тест найден"),
            @ApiResponse(responseCode = "404", description = "Тест не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TestResponseDto> getById(
            @Parameter(description = "Идентификатор теста", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(testService.findById(id));
    }

    @Operation(summary = "Создать тест",
            description = "Создаёт тест с вопросами и вариантами. Поле correct принимается от админа")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Тест создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PostMapping
    public ResponseEntity<TestResponseDto> create(@Valid @RequestBody TestRequestDto dto) {
        return ResponseEntity.ok(testService.save(dto));
    }

    @Operation(summary = "Обновить тест",
            description = "Обновляет существующий тест по id вместе с вопросами")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Тест обновлён"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "404", description = "Тест не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TestResponseDto> update(
            @Parameter(description = "Идентификатор теста", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody TestRequestDto dto) {
        return ResponseEntity.ok(testService.update(id, dto));
    }

    @Operation(summary = "Удалить тест", description = "Удаляет тест вместе с вопросами")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Тест удалён"),
            @ApiResponse(responseCode = "404", description = "Тест не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Идентификатор теста", example = "1")
            @PathVariable Long id) {
        testService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Сдать тест",
            description = "Принимает ответы студента, сравнивает с правильными и возвращает результат")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Результат сдачи"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "404", description = "Тест, вопрос или вариант не найдены"),
            @ApiResponse(responseCode = "409", description = "Тест уже был пройден")
    })
    @PostMapping("/submit")
    public ResponseEntity<TestResultDto> submit(@Valid @RequestBody TestSubmitDto dto) {
        return ResponseEntity.ok(testService.submitTest(dto));
    }
}
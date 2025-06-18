package org.example;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Основной класс Spring-boot приложения.
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Модуль 4.1.2 Swagger UI",
                version = "1.0",
                description = "Документация для API приложения - модуль 4.1.2"
        ))
@SpringBootApplication
public class Main {

    /**
     * Основной метод - точка входа в Spring-boot приложение.
     *
     * @param args аргументы командной строки.
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

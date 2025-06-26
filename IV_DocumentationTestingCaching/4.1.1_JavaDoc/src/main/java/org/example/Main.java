package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Основной класс Spring-boot приложения.
 */
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

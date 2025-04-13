package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilmorateApplication {
    public static void main(String[] args) {
        try {
            Class.forName("org.h2.Driver");
            System.out.println("H2 драйвер найден!");
        } catch (ClassNotFoundException e) {
            System.err.println("H2 драйвер НЕ найден!");
        }
        SpringApplication.run(FilmorateApplication.class, args);
    }
}


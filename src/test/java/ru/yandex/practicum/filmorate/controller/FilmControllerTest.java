package ru.yandex.practicum.filmorate.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateFilmValidationName() throws Exception {
        Film film = new Film();
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2023, 10, 26));
        film.setDuration(Duration.ofMinutes(150));

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());

        film.setName("name");

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateFilmValidationDescription() throws Exception {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description".repeat(20));
        film.setReleaseDate(LocalDate.of(2023, 10, 26));
        film.setDuration(Duration.ofMinutes(150));

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());

        film.setDescription("description");

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateFilmValidationReleaseDate() throws Exception {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(1777, 10, 26));
        film.setDuration(Duration.ofMinutes(150));

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());

        film.setReleaseDate(LocalDate.of(1895, 12, 29));

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateFilmValidationDuration() throws Exception {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2023, 10, 26));
        film.setDuration(Duration.ofMinutes(111));

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk());
    }
}
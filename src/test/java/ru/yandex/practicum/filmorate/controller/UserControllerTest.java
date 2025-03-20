package ru.yandex.practicum.filmorate.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateUserValidationEmail() throws Exception {
        User user = new User();
        user.setEmail("");
        user.setName("Totality");
        user.setLogin("DartVader");
        user.setBirthday(LocalDate.of(1999, 12, 31));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());

        user.setEmail("vader.ya.ru");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());

        user.setEmail("dartVader@ya.ru");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateUserValidationLogin() throws Exception {
        User user = new User();
        user.setEmail("dartVader@ya.ru");
        user.setName("Totality");
        user.setLogin("   ");
        user.setBirthday(LocalDate.of(1999, 12, 31));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());

        user.setLogin("DartVader");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateUserValidationName() throws Exception {
        User user = new User();
        user.setEmail("dartVader@ya.ru");
        user.setLogin("DartVader");
        user.setBirthday(LocalDate.of(1999, 12, 31));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateUserValidationBirthday() throws Exception {
        User user = new User();
        user.setEmail("dartVader@ya.ru");
        user.setLogin("DartVader");
        user.setBirthday(LocalDate.of(2222, 12, 31));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());

        user.setBirthday(LocalDate.of(2025, 3, 19));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }
}
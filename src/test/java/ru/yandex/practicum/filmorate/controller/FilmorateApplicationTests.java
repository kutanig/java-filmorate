package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;


@AutoConfigureTestDatabase
@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;

    private User testUser;
    private Film testFilm;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .email("test@example.com")
                .login("testLogin")
                .name("Test User")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        testFilm = Film.builder()
                .name("Test Film")
                .description("Test Description")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(120)
                .mpa(new Mpa(1L, "G", "Нет возрастных ограничений"))
                .build();
    }

    // User Tests
    @Test
    void shouldCreateAndRetrieveUser() {
        User createdUser = userStorage.add(testUser);
        assertThat(createdUser.getId()).isNotNull();

        User retrievedUser = userStorage.getUserById(createdUser.getId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        assertThat(retrievedUser).isEqualTo(createdUser);
    }

    @Test
    void shouldUpdateUser() {
        User createdUser = userStorage.add(testUser);
        createdUser.setName("Updated Name");
        userStorage.update(createdUser);

        User updatedUser = userStorage.getUserById(createdUser.getId()).orElseThrow();
        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
    }

    @Test
    void shouldReturnEmptyForNonExistingUser() {
        assertThat(userStorage.getUserById(-999L)).isEmpty();
    }

    @Test
    void shouldUpdateFilm() {
        Film createdFilm = filmStorage.add(testFilm);
        createdFilm.setName("Updated Film Name");
        filmStorage.update(createdFilm);

        Film updatedFilm = filmStorage.getFilmById(createdFilm.getId()).orElseThrow();
        assertThat(updatedFilm.getName()).isEqualTo("Updated Film Name");
    }

    @Test
    void shouldAddAndRemoveLikes() {
        User user = userStorage.add(testUser);
        Film film = filmStorage.add(testFilm);

        filmStorage.addLike(film.getId(), user.getId());
        assertThat(filmStorage.getLikesCount(film)).isEqualTo(1);

        filmStorage.deleteLike(film.getId(), user.getId());
        assertThat(filmStorage.getLikesCount(film)).isZero();
    }

    // Genre & MPA Tests
    @Test
    void shouldReturnAllGenres() {
        Collection<Genre> genres = filmStorage.getGenres();
        assertThat(genres)
                .extracting(Genre::getName)
                .containsExactlyInAnyOrder(
                        "Комедия", "Драма", "Мультфильм", "Триллер", "Документальный", "Боевик");
    }

    @Test
    void shouldReturnAllMPAs() {
        Collection<Mpa> mpas = filmStorage.getMPAs();
        assertThat(mpas)
                .extracting(Mpa::getName)
                .containsExactlyInAnyOrder("G", "PG", "PG-13", "R", "NC-17");
    }

    @Test
    void shouldHandleFilmWithGenres() {
        Film film = filmStorage.add(testFilm);
        film.setGenres(List.of(new Genre(1L, "Комедия"), new Genre(2L, "Драма")));
        filmStorage.update(film);

        Film updatedFilm = filmStorage.getFilmById(film.getId()).orElseThrow();
        assertThat(updatedFilm.getGenres())
                .extracting(Genre::getId)
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void shouldThrowWhenNotFound() {
        assertAll(
                () -> assertThrows(NotFoundException.class,
                        () -> filmStorage.getFilmById(-999L).orElseThrow(() -> new NotFoundException(""))),
                () -> assertThrows(NotFoundException.class,
                        () -> userStorage.getUserById(-999L).orElseThrow(() -> new NotFoundException("")))
        );
    }

    //Film test
    @Test
    void shouldKeepSameIdAfterFilmUpdate() {
        // Создаем фильм
        Film createdFilm = filmStorage.add(testFilm);
        Long originalId = createdFilm.getId();

        // Обновляем данные фильма
        createdFilm.setName("New Name");
        createdFilm.setDescription("New Description");
        Film updatedFilm = filmStorage.update(createdFilm);

        // Проверяем, что ID остался прежним
        assertThat(updatedFilm.getId())
                .as("ID фильма должен оставаться неизменным после обновления")
                .isEqualTo(originalId);

        // Дополнительная проверка: получаем фильм из хранилища и проверяем ID
        Film retrievedFilm = filmStorage.getFilmById(originalId).orElseThrow();
        assertThat(retrievedFilm.getId())
                .isEqualTo(originalId);
    }
}
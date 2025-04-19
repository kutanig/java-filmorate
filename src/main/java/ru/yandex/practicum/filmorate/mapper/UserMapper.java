package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

public class UserMapper {
    public static UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setLogin(user.getLogin());
        dto.setName(user.getName() != null && !user.getName().isBlank()
                ? user.getName()
                : user.getLogin());
        dto.setBirthday(user.getBirthday());
        return dto;
    }

    public static User toUser(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setLogin(dto.getLogin().trim());
        user.setName(dto.getName() != null && !dto.getName().isBlank()
                ? dto.getName()
                : dto.getLogin());
        user.setBirthday(dto.getBirthday());
        return user;
    }
}

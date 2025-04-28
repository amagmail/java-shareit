package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    private Long id;

    @NotEmpty(message = "Имя пользователя не может быть пустым")
    private String name;

    @NotEmpty(message = "Почта пользователя не может быть пустым")
    @Email(message = "Электронная почта пользователя неверного формата")
    private String email;

}

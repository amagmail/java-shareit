package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {

    UserDto create(UserDto entity);

    UserDto update(Long id, UserDto entity);

    void remove(Long id);

    UserDto getItem(Long id);

    Collection<UserDto> getItems();

}

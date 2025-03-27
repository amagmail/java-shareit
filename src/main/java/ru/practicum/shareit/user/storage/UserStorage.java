package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserStorage {

    User create(User user);

    User update(Long id, User user);

    void remove(Long id);

    User getItem(Long id);

    Collection<User> getItems();

}

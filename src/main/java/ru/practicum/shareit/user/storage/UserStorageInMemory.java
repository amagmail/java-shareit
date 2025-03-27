package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UserStorageInMemory implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User create(User entity) {
        String email = entity.getEmail();
        if (getItemByEmail(email).isPresent()) {
            throw new DuplicateException("Пользователь с почтой " + email + " существует");
        }
        entity.setId(getNextId());
        users.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public User update(Long id, User entity) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь с идентификатором " + id + " не найден");
        }
        User oldUser = users.get(id);
        String userEmail = entity.getEmail();
        String userName = entity.getName();
        if (userEmail != null && !userEmail.isBlank()) {
            if (getItemByEmail(userEmail).isPresent()) {
                throw new DuplicateException("Пользователь с почтой " + userEmail + " существует");
            }
            oldUser.setEmail(userEmail);
        }
        if (userName != null && !userName.isBlank()) {
            oldUser.setName(userName);
        }
        return oldUser;
    }

    @Override
    public void remove(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь с идентификатором " + id + " не найден");
        }
        users.remove(id);
    }

    @Override
    public User getItem(Long id) {
        if (!users.containsKey(id)) {
            throw new DuplicateException("Пользователь с идентификатором " + id + " существует");
        }
        return users.get(id);
    }

    @Override
    public Collection<User> getItems() {
        return users.values();
    }

    // Вспомогательный метод для получения пользователя по почте
    public Optional<User> getItemByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    // Вспомогательный метод для генерации идентификатора объекта
    private long getNextId() {

        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);

        return ++currentMaxId;
    }

}

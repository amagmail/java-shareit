package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid UserDto entity) {
        log.info("Создать пользователя из данных {}", entity);
        return userClient.create(entity);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody UserDto entity) {
        log.info("Обновить пользователя из данных {} по идентификатору {}", entity, id);
        return userClient.update(id, entity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> remove(@PathVariable("id") Long id) {
        log.info("Удалить пользователя по идентификатору {}", id);
        return userClient.remove(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(@PathVariable("id") Long id) {
        log.info("Получить пользователя по идентификатору {}", id);
        return userClient.getItem(id);
    }

    @GetMapping
    public ResponseEntity<Object> getItems() {
        log.info("Получить всех пользователей");
        return userClient.getItems();
    }

}

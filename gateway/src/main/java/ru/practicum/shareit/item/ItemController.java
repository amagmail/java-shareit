package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("id") Long itemId, @RequestBody @Valid CommentDto entity) {
        log.info("Создать комментарий из данных {} для предмета {}, пользователь {}", entity, itemId, userId);
        return itemClient.addComment(userId, itemId, entity);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @Valid ItemDto entity) {
        log.info("Создать предмет из данных {}, пользователь {}", entity, userId);
        return itemClient.create(userId, entity);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("id") Long itemId, @RequestBody ItemDto entity) {
        log.info("Обновить предмет из данных {} по идентификатору {}, пользователь {}", entity, itemId, userId);
        return itemClient.update(userId, itemId, entity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("id") Long itemId) {
        log.info("Получить предмет по идентификатору {}, пользователь {}", itemId, userId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получить предметы, пользователь {}", userId);
        return itemClient.getItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getSearch(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam String text) {
        log.info("Осуществить поиск предмета по тексту {}, пользователь {}", text, userId);
        return itemClient.getSearch(userId, text);
    }

}

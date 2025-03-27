package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@RequestHeader("X-Later-User-Id") Long userId, @RequestBody @Valid ItemDto entity) {
        return itemService.create(userId, entity);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader("X-Later-User-Id") Long userId, @PathVariable("id") Long itemId, @RequestBody ItemDto entity) {
        return itemService.update(userId, itemId, entity);
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@RequestHeader("X-Later-User-Id") Long userId, @PathVariable("id") Long itemId) {
        return itemService.getItem(userId, itemId);
    }

    @GetMapping
    public Collection<ItemDto> getItems(@RequestHeader("X-Later-User-Id") Long userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> getSearch(@RequestHeader("X-Later-User-Id") Long userId, @RequestParam String text) {
        return itemService.getSearch(userId, text);
    }

}

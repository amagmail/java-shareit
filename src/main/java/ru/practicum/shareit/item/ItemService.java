package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {

    ItemDto create(Long userId, ItemDto entity);

    ItemDto update(Long userId, Long itemId, ItemDto entity);

    ItemDto getItem(Long userId, Long itemId);

    Collection<ItemDto> getItems(Long userId);

    Collection<ItemDto> getSearch(Long userId, String text);

}

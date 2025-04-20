package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDataDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {

    CommentDto addComment(Long userId, Long itemId, CommentDto entity);

    ItemDto create(Long userId, ItemDto entity);

    ItemDto update(Long userId, Long itemId, ItemDto entity);

    ItemDataDto getItem(Long userId, Long itemId);

    Collection<ItemDto> getItems(Long userId);

    Collection<ItemDto> getSearch(Long userId, String text);

}

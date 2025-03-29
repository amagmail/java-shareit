package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {

    Item create(Long userId, Item item);

    Item update(Long userId, Long itemId, Item item);

    Item getItem(Long userId, Long itemId);

    Collection<Item> getItems(Long userId);

    Collection<Item> getSearch(Long userId, String text);

}

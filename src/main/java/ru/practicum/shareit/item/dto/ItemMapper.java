package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public final class ItemMapper {

    public static ItemDto toItemDto(Item item) {

        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        return dto;
    }

    public static Item toItemModel(ItemDto dto) {

        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        return item;
    }

}

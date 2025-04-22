package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;

@UtilityClass
public class RequestMapper {

    public static RequestDto toRequestDto(Request request) {

        RequestDto dto = new RequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setRequestorName(request.getRequestor().getName());
        dto.setCreated(request.getCreated());
        return dto;
    }

    public static Request toRequestModel(RequestDto dto) {

        Request request = new Request();
        request.setId(dto.getId());
        request.setDescription(dto.getDescription());
        return request;
    }

    public static RequestItemDto toRequestItemDto(Item item) {

        RequestItemDto dto = new RequestItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setOwner(item.getOwner());
        return dto;
    }

    /*
    public static Item toRequestItemModel(RequestItemDto dto) {

        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setOwner(dto.getOwner());
        return item;
    } */

}

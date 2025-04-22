package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestItemDto {

    private Long id;
    private String name;
    private Long owner;

}

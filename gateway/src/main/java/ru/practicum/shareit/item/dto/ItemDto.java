package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemDto {

    private Long id;
    private Long requestId;

    @NotEmpty(message = "Название предмета не может быть пустым")
    private String name;

    @NotEmpty(message = "Описание предмета не может быть пустым")
    private String description;

    @NotNull(message = "Признак доступности предмета не может быть пустым")
    private Boolean available;

}

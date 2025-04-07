package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemDto {

    private Long id;

    @NotEmpty(message = "Наименование не может быть пустым")
    private String name;

    @NotEmpty(message = "Описание не может быть пустым")
    private String description;

    @NotNull(message = "Признак доступности не может быть пустым")
    private Boolean available;

}

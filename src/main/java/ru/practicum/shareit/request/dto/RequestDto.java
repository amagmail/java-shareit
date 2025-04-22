package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@NoArgsConstructor
public class RequestDto {

    private Long id;
    private String description;
    private String requestorName;
    private LocalDateTime created;
    private Collection<RequestItemDto> items;

}

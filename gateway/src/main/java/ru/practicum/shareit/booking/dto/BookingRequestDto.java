package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.validators.DateRangeControl;

import java.time.LocalDateTime;

@Data
@Builder
@DateRangeControl
public class BookingRequestDto {

    @NotNull(message = "Идентификатор предмета не может быть пустым")
    private Long itemId;

    @NotNull(message = "Дата начала бронирования не может быть пустым")
    @Future(message = "Дата начала бронирования должна быть больше текущей даты")
    private LocalDateTime start;

    @NotNull(message = "Дата окончания бронирования не может быть пустым")
    @Future(message = "Дата окончания бронирования должна быть больше текущей даты")
    private LocalDateTime end;

}

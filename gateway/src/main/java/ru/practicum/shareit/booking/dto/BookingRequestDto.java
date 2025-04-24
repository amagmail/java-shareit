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

    @NotNull(message = "Необходимо указать идентификатор")
    private Long itemId;

    @NotNull(message = "Необходимо указать дату начала бронирования")
    @Future(message = "Необходимо выбрать дату больше текущего времени")
    private LocalDateTime start;

    @NotNull(message = "Необходимо указать дату окончания бронирования")
    @Future(message = "Необходимо выбрать дату больше текущего времени")
    private LocalDateTime end;

}

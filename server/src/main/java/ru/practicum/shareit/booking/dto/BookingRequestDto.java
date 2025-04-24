package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.validators.DateRangeControl;

import java.time.LocalDateTime;

@Data
@Builder
@DateRangeControl
public class BookingRequestDto {

    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;

}

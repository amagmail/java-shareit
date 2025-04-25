package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.validators.DateRangeControl;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@DateRangeControl
public class BookingRequestDto {

    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;

}

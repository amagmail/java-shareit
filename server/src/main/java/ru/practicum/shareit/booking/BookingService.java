package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.enums.BookingState;

import java.util.Collection;

public interface BookingService {

    BookingDto createBooking(Long userId, BookingRequestDto entity);

    BookingDto approveBooking(Long userId, Long bookingId, boolean approved);

    BookingDto getBookingByID(Long userId, Long bookingId);

    Collection<BookingDto> getBookingAll(Long userId, BookingState state);

    Collection<BookingDto> getBookingAllByOwner(Long userId, BookingState state);

}

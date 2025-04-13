package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.Collection;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingStorage bookingStorage;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    @Override
    public BookingDto createBooking(Long userId, BookingRequestDto entity) {
        Long id = entity.getItemId();
        User user = userStorage.findById(userId).orElseThrow(() -> new AccessDeniedException("Пользователь с идентификатором " + userId + " не найден"));
        Item item = itemStorage.findById(id).orElseThrow(() -> new NotFoundException("Предмет с идентификатором " + id + " не найден"));
        if (!item.getAvailable()) {
            throw new ValidationException("Предмет с идентификторо " + id + " недоступна для бронирования");
        }
        if (item.getOwner().equals(user.getId())) {
            throw new NotFoundException("Владелец не может бронировать свой предмет");
        }
        Booking booking = BookingMapper.toBookingModel(entity);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        booking = bookingStorage.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto approveBooking(Long userId, Long bookingId, boolean approved) {
        userStorage.findById(userId).orElseThrow(() -> new AccessDeniedException("Пользователь с идентификатором " + userId + " не найден"));
        Booking booking = bookingStorage.findById(bookingId).orElseThrow(() -> new NotFoundException("Бронирование с идентификатором " + bookingId + " не найдено"));
        if (!booking.getItem().getOwner().equals(userId)) {
            throw new NotFoundException("Пользователь " + userId + " не является владельцем предмета " + booking.getItem().getId());
        }
        BookingStatus status = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        booking.setStatus(status);
        booking = bookingStorage.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getBookingByID(Long userId, Long bookingId) {
        User user = userStorage.findById(userId).orElseThrow(() -> new AccessDeniedException("Пользователь с идентификатором " + userId + " не найден"));
        Booking booking = bookingStorage.findById(bookingId).orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + bookingId + " не найден"));
        if (!booking.getBooker().getId().equals(user.getId()) && !booking.getItem().getOwner().equals(user.getId())) {
            throw new NotFoundException("Не удалось найти соответствующую запись по бронированию");
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public Collection<BookingDto> getBookingAll(Long userId, BookingState state) {
        User user = userStorage.findById(userId).orElseThrow(() -> new AccessDeniedException("Пользователь с идентификатором " + userId + " не найден"));
        Collection<Booking> bookings;
        switch (state) {
            case CURRENT:
                bookings = bookingStorage.findAllByBookerCurrent(user, LocalDateTime.now());
                break;
            case PAST:
                bookings = bookingStorage.findAllByBookerPast(user, LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingStorage.findAllByBookerFuture(user, LocalDateTime.now());
                break;
            case WAITING:
                bookings = bookingStorage.findAllByBookerStatus(user, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingStorage.findAllByBookerStatus(user, BookingStatus.REJECTED);
                break;
            default:
                bookings = bookingStorage.findAllByBooker(user);
        };
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public Collection<BookingDto> getBookingAllByOwner(Long userId, BookingState state) {
        User user = userStorage.findById(userId).orElseThrow(() -> new AccessDeniedException("Пользователь с идентификатором " + userId + " не найден"));
        Collection<Booking> bookings;
        switch (state) {
            case CURRENT:
                bookings = bookingStorage.findAllByOwnerCurrent(user, LocalDateTime.now());
                break;
            case PAST:
                bookings = bookingStorage.findAllByOwnerPast(user, LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingStorage.findAllByOwnerFuture(user, LocalDateTime.now());
                break;
            case WAITING:
                bookings = bookingStorage.findAllByOwnerStatus(user, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingStorage.findAllByOwnerStatus(user, BookingStatus.REJECTED);
                break;
            default:
                bookings = bookingStorage.findAllByOwner(user);
        };
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

}

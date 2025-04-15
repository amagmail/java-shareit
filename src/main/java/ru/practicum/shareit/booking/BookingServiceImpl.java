package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dal.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto createBooking(Long userId, BookingRequestDto entity) {
        Long id = entity.getItemId();
        User user = userRepository.findById(userId).orElseThrow(() -> new AccessDeniedException("Пользователь с идентификатором " + userId + " не найден"));
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Предмет с идентификатором " + id + " не найден"));
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
        booking = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto approveBooking(Long userId, Long bookingId, boolean approved) {
        userRepository.findById(userId).orElseThrow(() -> new AccessDeniedException("Пользователь с идентификатором " + userId + " не найден"));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Бронирование с идентификатором " + bookingId + " не найдено"));
        if (!booking.getItem().getOwner().equals(userId)) {
            throw new NotFoundException("Пользователь " + userId + " не является владельцем предмета " + booking.getItem().getId());
        }
        BookingStatus status = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        booking.setStatus(status);
        booking = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getBookingByID(Long userId, Long bookingId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AccessDeniedException("Пользователь с идентификатором " + userId + " не найден"));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + bookingId + " не найден"));
        if (!booking.getBooker().getId().equals(user.getId()) && !booking.getItem().getOwner().equals(user.getId())) {
            throw new NotFoundException("Не удалось найти соответствующую запись по бронированию");
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public Collection<BookingDto> getBookingAll(Long userId, BookingState state) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AccessDeniedException("Пользователь с идентификатором " + userId + " не найден"));
        Collection<Booking> bookings;
        switch (state) {
            case CURRENT:
                bookings = bookingRepository.findAllByBookerCurrent(user, LocalDateTime.now());
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerPast(user, LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerFuture(user, LocalDateTime.now());
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBookerStatus(user, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerStatus(user, BookingStatus.REJECTED);
                break;
            default:
                bookings = bookingRepository.findAllByBooker(user);
        };
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public Collection<BookingDto> getBookingAllByOwner(Long userId, BookingState state) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AccessDeniedException("Пользователь с идентификатором " + userId + " не найден"));
        Collection<Booking> bookings;
        switch (state) {
            case CURRENT:
                bookings = bookingRepository.findAllByOwnerCurrent(user, LocalDateTime.now());
                break;
            case PAST:
                bookings = bookingRepository.findAllByOwnerPast(user, LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByOwnerFuture(user, LocalDateTime.now());
                break;
            case WAITING:
                bookings = bookingRepository.findAllByOwnerStatus(user, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByOwnerStatus(user, BookingStatus.REJECTED);
                break;
            default:
                bookings = bookingRepository.findAllByOwner(user);
        };
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

}

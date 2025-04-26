package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.enums.BookingState;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @Valid BookingRequestDto entity) {
		log.info("Создать бронирование из данных {}, пользователь {}", entity, userId);
		return bookingClient.createBooking(userId, entity);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Object> approveBooking(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("id") Long bookingId, @RequestParam Boolean approved) {
		log.info("Подтвердить бронирование по идентификатору {}, пользователь {}", bookingId, userId);
		return bookingClient.approveBooking(userId, bookingId, approved);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getBookingByID(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("id") Long bookingId) {
		log.info("Получить бронирование по идентификатору {}, пользователь {}", bookingId, userId);
		return bookingClient.getBookingByID(userId, bookingId);
	}

	@GetMapping
	public ResponseEntity<Object> getBookingAll(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(required = false, defaultValue = "ALL") BookingState state) {
		log.info("Получить все бронирования по статусу {}, пользователь {}", state, userId);
		return bookingClient.getBookingAll(userId, state);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getBookingAllByOwner(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(required = false, defaultValue = "ALL") BookingState state) {
		log.info("Получить бронирования владельца по статусу {}, пользователь {}", state, userId);
		return bookingClient.getBookingAllByOwner(userId, state);
	}

}

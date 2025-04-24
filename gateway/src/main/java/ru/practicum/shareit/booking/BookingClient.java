package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createBooking(long userId, BookingRequestDto entity) {
        return post("", userId, entity);
    }

    public ResponseEntity<Object> approveBooking(long userId, Long bookingId, Boolean approved) {
        /*
        Map<String, Object> parameters = Map.of(
                "approved", approved
        );
        return patch("/" + bookingId, userId, parameters);
        */
        return patch("/" + bookingId + "?approved=" + approved, userId);
    }

    public ResponseEntity<Object> getBookingByID(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getBookingAll(long userId, BookingState state) {
        /*
        Map<String, Object> parameters = Map.of(
                "state", state
        );
        return get("", userId, parameters); */
        return get("?state=" + state.name(), userId);
    }

    public ResponseEntity<Object> getBookingAllByOwner(long userId, BookingState state) {
        /*
        Map<String, Object> parameters = Map.of(
                "state", state
        );
        return get("/owner", userId, parameters); */
        return get("/owner?state=" + state.name(), userId);
    }

}

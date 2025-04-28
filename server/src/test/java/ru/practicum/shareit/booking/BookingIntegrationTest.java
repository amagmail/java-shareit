package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ShareItServer.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookingIntegrationTest {

    @Autowired
    private UserController userController;

    @Autowired
    private ItemController itemController;

    @Autowired
    private BookingController bookingController;

    @Test
    void createBooking() {

        UserDto u1 = new UserDto();
        u1.setName("USER-1");
        u1.setEmail("USER-1@mail.ru");
        u1 = userController.create(u1);

        UserDto u2 = new UserDto();
        u2.setName("USER-2");
        u2.setEmail("USER-2@mail.ru");
        u2 = userController.create(u2);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("ITEM");
        itemDto.setDescription("TEXT");
        itemDto.setAvailable(true);
        itemDto = itemController.create(u1.getId(), itemDto);

        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(itemDto.getId());
        bookingRequestDto.setStart(LocalDateTime.now());
        bookingRequestDto.setEnd(LocalDateTime.now().plusDays(1));

        BookingDto bookingDto = bookingController.createBooking(u2.getId(), bookingRequestDto);
        assertEquals(bookingRequestDto.getItemId(), bookingDto.getItem().getId(), "Ошибка проверки");
        assertEquals(u2.getId(), bookingDto.getBooker().getId(), "Ошибка проверки");
    }

    @Test
    void getBooking() {
        UserDto u1 = new UserDto();
        u1.setName("USER-1");
        u1.setEmail("USER-1@mail.ru");
        u1 = userController.create(u1);

        UserDto u2 = new UserDto();
        u2.setName("USER-2");
        u2.setEmail("USER-2@mail.ru");
        u2 = userController.create(u2);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("ITEM");
        itemDto.setDescription("TEXT");
        itemDto.setAvailable(true);
        itemDto = itemController.create(u1.getId(), itemDto);

        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(itemDto.getId());
        bookingRequestDto.setStart(LocalDateTime.now());
        bookingRequestDto.setEnd(LocalDateTime.now().plusDays(1));

        BookingDto b1 = bookingController.createBooking(u2.getId(), bookingRequestDto);
        BookingDto b2 = bookingController.getBookingByID(u2.getId(), b1.getId());
        assertEquals(b1.getId(), b2.getId(), "Ошибка проверки");
    }

    @Test
    void approveBooking() {
        UserDto u1 = new UserDto();
        u1.setName("USER-1");
        u1.setEmail("USER-1@mail.ru");
        u1 = userController.create(u1);

        UserDto u2 = new UserDto();
        u2.setName("USER-2");
        u2.setEmail("USER-2@mail.ru");
        u2 = userController.create(u2);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("ITEM");
        itemDto.setDescription("TEXT");
        itemDto.setAvailable(true);
        itemDto = itemController.create(u1.getId(), itemDto);

        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(itemDto.getId());
        bookingRequestDto.setStart(LocalDateTime.now());
        bookingRequestDto.setEnd(LocalDateTime.now().plusDays(1));

        BookingDto b1 = bookingController.createBooking(u2.getId(), bookingRequestDto);
        BookingDto b2 = bookingController.approveBooking(u1.getId(), b1.getId(), true);
        assertEquals(BookingStatus.APPROVED, b2.getStatus(), "Ошибка проверки");
    }

}

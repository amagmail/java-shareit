package ru.practicum.shareit.booking;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.user.dal.UserRepository;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private final LocalDateTime now = LocalDateTime.now();
    private final LocalDateTime future = now.plusDays(1);
    private final LocalDateTime past = now.minusDays(1);

    /*
    @Test
    void createShouldThrowWhenEndTimeBeforeStartTime() {
        Long userId = 1L;

        User booker = new User(); //createUser(userId, "Booker", "booker@email.com");
        booker.setId(userId);
        booker.setName("BOOKER");
        booker.setEmail("BOOKER@mail.ru");

        User owner = new User(); //createUser(2L, "Owner", "owner@email.com");
        booker.setId(2L);
        booker.setName("OWNER");
        booker.setEmail("OWNER@mail.ru");

        Item item = new Item(); //createItem(1L, "Item", owner, true);
        item.setId(1L);
        item.setName("ITEM");
        item.setOwner(2L);
        item.setAvailable(true);

        BookingRequestDto booking = new BookingRequestDto();
        booking.setItemId(1L);
        booking.setStart(future);
        booking.setEnd(past);
    } */

}

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
    void createShouldThrowWhenItemNotFound() {
        Long userId = 1L;

        BookingRequestDto booking = new BookingRequestDto();
        booking.setItemId(1L);
        booking.setStart(future);
        booking.setEnd(past);

        User owner = new User();
        owner.setId(2L);
        owner.setName("OWNER");
        owner.setEmail("OWNER@mail.ru");

        Item item = new Item();
        item.setId(1L);
        item.setName("ITEM");
        item.setOwner(2L);
        item.setAvailable(true);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(AccessDeniedException.class, () -> bookingService.createBooking(userId, booking));
        verify(itemRepository).findById(anyLong());
        verify(userRepository).findById(userId);
        verifyNoInteractions(bookingRepository);
    } */
}

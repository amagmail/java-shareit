package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ShareItServer.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserIntegrationTest {

    @Autowired
    private UserController userController;

    @Autowired
    private ItemController itemController;

    @Autowired
    private BookingController bookingController;

    @Test
    void createUser() {
        UserDto user = new UserDto();
        user.setName("USER");
        user.setEmail("USER@mail.ru");
        user = userController.create(user);

        assertNotNull(user.getId());
    }

    @Test
    void getUser() {
        UserDto u1 = new UserDto();
        u1.setName("USER");
        u1.setEmail("USER@mail.ru");
        u1 = userController.create(u1);

        UserDto u2 = userController.getItem(u1.getId());
        assertEquals(u1, u2);
    }

    @Test
    void updatesUser() {
        UserDto u1 = new UserDto();
        u1.setName("USER-1");
        u1.setEmail("USER-1@mail.ru");
        u1 = userController.create(u1);

        u1.setName("USER-2");
        u1.setEmail("USER-2@mail.ru");
        UserDto u2 = userController.update(u1.getId(), u1);

        assertEquals(u1, u2);
    }

    @Test
    void deletesUser() {
        UserDto user = new UserDto();
        user.setName("USER");
        user.setEmail("USER@mail.ru");
        user = userController.create(user);

        Long id = user.getId();
        userController.remove(id);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> userController.getItem(id), "Ошибка проверки");
        assertTrue(thrown.getMessage().contains("Пользователь с идентификатором " + id + " не найден"), "Ошибка проверки");
    }

}

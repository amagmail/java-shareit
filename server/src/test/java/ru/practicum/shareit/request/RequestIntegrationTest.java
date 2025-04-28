package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = ShareItServer.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RequestIntegrationTest {

    @Autowired
    private UserController userController;

    @Autowired
    private ItemController itemController;

    @Autowired
    private RequestController requestController;

    @Test
    void createRequest() {
        UserDto user = new UserDto();
        user.setName("USER");
        user.setEmail("USER@mail.ru");
        user = userController.create(user);

        RequestDto request = new RequestDto();
        request.setDescription("TEXT");
        request = requestController.create(user.getId(), request);

        assertNotNull(request.getId(), "Ошибка проверки");
        assertEquals("TEXT", request.getDescription(), "Ошибка проверки");
    }

    @Test
    void getRequest() {
        UserDto u1 = new UserDto();
        u1.setName("USER-1");
        u1.setEmail("USER-1@mail.ru");
        u1 = userController.create(u1);

        UserDto u2 = new UserDto();
        u2.setName("USER-2");
        u2.setEmail("USER-2@mail.ru");
        u2 = userController.create(u2);

        RequestDto request = new RequestDto();
        request.setDescription("TEXT");
        request = requestController.create(u1.getId(), request);

        ItemDto item = new ItemDto();
        item.setName("ITEM");
        item.setDescription("TEXT");
        item.setAvailable(true);
        item.setRequestId(request.getId());
        itemController.create(u2.getId(), item);

        RequestDto r2 = requestController.getRequest(u1.getId(), request.getId());
        assertNotNull(r2, "Ошибка проверки");
        assertEquals(request.getId(), r2.getId(), "Ошибка проверки");
        assertEquals(1, r2.getItems().size(), "Ошибка проверки");
    }

}

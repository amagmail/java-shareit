package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @Valid RequestDto entity) {
        log.info("Создать запрос из данных {}, пользователь {}", entity, userId);
        return requestClient.create(userId, entity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRequest(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("id") Long requestId) {
        log.info("Получить запрос по идентификатору {}, пользователь {}", requestId, userId);
        return requestClient.getRequest(userId, requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getMyRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получить все мои запросы, пользователь {}", userId);
        return requestClient.getMyRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получить все запросы кроме моих, пользователь {}", userId);
        return requestClient.getAllRequests(userId);
    }

}

package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public RequestDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid RequestDto entity) {
        return requestService.create(userId, entity);
    }

    @GetMapping("/{id}")
    public RequestDto getRequest(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("id") Long requestId) {
        return requestService.getRequest(userId, requestId);
    }

    @GetMapping
    public Collection<RequestDto> getMyRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.getMyRequests(userId);
    }

    @GetMapping("/all")
    public Collection<RequestDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.getAllRequests(userId);
    }

}

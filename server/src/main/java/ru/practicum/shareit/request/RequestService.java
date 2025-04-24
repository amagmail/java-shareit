package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.RequestDto;

import java.util.Collection;

public interface RequestService {

    RequestDto create(Long userId, RequestDto entity);

    RequestDto getRequest(Long userId, Long requestId);

    Collection<RequestDto> getMyRequests(Long userId);

    Collection<RequestDto> getAllRequests(Long userId);

}

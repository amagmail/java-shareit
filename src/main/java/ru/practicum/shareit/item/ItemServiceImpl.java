package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDataDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final CommentStorage commentStorage;
    private final BookingStorage bookingStorage;

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto entity) {
        User user = userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));
        Item item = itemStorage.findById(itemId).orElseThrow(() -> new NotFoundException("Предмет с идентификатором " + itemId + " не найден"));
        Booking booking = bookingStorage.findAllByItemAndBookerPast(item, user, LocalDateTime.now()).stream()
                .findFirst()
                .orElseThrow(() -> new ValidationException("Пользователь " + userId + " не брал предмет " + itemId + " в аренду"));
        if (!booking.getStatus().equals(BookingStatus.APPROVED) || booking.getEnd().isAfter(LocalDateTime.now())) {
            throw new ValidationException("Пользователь " + userId + " не брал предмет " + itemId + " в аренду");
        }
        Comment comment = ItemMapper.toCommentModel(entity);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        comment = commentStorage.save(comment);
        return ItemMapper.toCommentDto(comment);
    }

    @Override
    public ItemDto create(Long userId, ItemDto entity) {
        userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));
        Item item = ItemMapper.toItemModel(entity);
        item.setOwner(userId);
        item = itemStorage.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto entity) {
        User user = userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));
        Item item = itemStorage.findById(itemId).orElseThrow(() -> new NotFoundException("Предмет с идентификатором " + itemId + " не найден"));
        if (!user.getId().equals(item.getOwner())) {
            throw new ValidationException("Предмет с идентификатором " + item.getId() + " не принадлежит пользователю с идентификатором " + user.getId());
        }
        if (entity.getName() != null) {
            item.setName(entity.getName());
        }
        if (entity.getDescription() != null) {
            item.setDescription(entity.getDescription());
        }
        if (entity.getAvailable() != null) {
            item.setAvailable(entity.getAvailable());
        }
        item = itemStorage.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDataDto getItem(Long userId, Long itemId) {
        User user = userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));
        Item item = itemStorage.findById(itemId).orElseThrow(() -> new NotFoundException("Предмет с идентификатором " + itemId + " не найден"));
        ItemDataDto itemData = ItemMapper.toItemDataDto(item);
        Collection<Booking> bookings = bookingStorage.findAllByItemOrderByStartAsc(item);
        boolean isOwner = user.getId().equals(item.getOwner());
        boolean isBooker = false;
        for (Booking booking : bookings) {
            if (isOwner && booking.getEnd().isBefore(LocalDateTime.now())) {
                itemData.setLastBooking(BookingMapper.toBookingDto(booking));
            }
            if (isOwner && booking.getStart().isAfter(LocalDateTime.now())) {
                itemData.setNextBooking(BookingMapper.toBookingDto(booking));
            }
            if (booking.getBooker().getId().equals(userId)) {
                isBooker = true;
            }
        }
        if (!isBooker && !isOwner) {
            throw new AccessDeniedException("Отказано в доступе");
        }
        List<CommentDto> comments = commentStorage.findAllByItem(item).stream()
                .map(ItemMapper::toCommentDto)
                .toList();
        itemData.setComments(comments);
        return itemData;
    }

    @Override
    public Collection<ItemDto> getItems(Long userId) {
        userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));
        return itemStorage.findAllByOwner(userId).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public Collection<ItemDto> getSearch(Long userId, String text) {
        userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));
        if (text.isEmpty() || text.isBlank()) {
            return List.of();
        }
        return itemStorage.getSearch(text).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

}

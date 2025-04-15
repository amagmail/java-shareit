package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDataDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dal.CommentRepository;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dal.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto entity) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Предмет с идентификатором " + itemId + " не найден"));
        Booking booking = bookingRepository.findAllByItemAndBookerPast(item, user, LocalDateTime.now()).stream()
                .findFirst()
                .orElseThrow(() -> new ValidationException("Пользователь " + userId + " не брал предмет " + itemId + " в аренду"));
        if (!booking.getStatus().equals(BookingStatus.APPROVED) || booking.getEnd().isAfter(LocalDateTime.now())) {
            throw new ValidationException("Пользователь " + userId + " не брал предмет " + itemId + " в аренду");
        }
        Comment comment = ItemMapper.toCommentModel(entity);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        comment = commentRepository.save(comment);
        return ItemMapper.toCommentDto(comment);
    }

    @Override
    public ItemDto create(Long userId, ItemDto entity) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));
        Item item = ItemMapper.toItemModel(entity);
        item.setOwner(userId);
        item = itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto entity) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Предмет с идентификатором " + itemId + " не найден"));
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
        item = itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDataDto getItem(Long userId, Long itemId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Предмет с идентификатором " + itemId + " не найден"));
        ItemDataDto itemData = ItemMapper.toItemDataDto(item);
        Collection<Booking> bookings = bookingRepository.findAllByItemOrderByStartAsc(item);
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
        List<CommentDto> comments = commentRepository.findAllByItem(item).stream()
                .map(ItemMapper::toCommentDto)
                .toList();
        itemData.setComments(comments);
        return itemData;
    }

    @Override
    public Collection<ItemDto> getItems(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));
        return itemRepository.findAllByOwner(userId).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public Collection<ItemDto> getSearch(Long userId, String text) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));
        if (text.isEmpty() || text.isBlank()) {
            return List.of();
        }
        return itemRepository.getSearch(text).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

}

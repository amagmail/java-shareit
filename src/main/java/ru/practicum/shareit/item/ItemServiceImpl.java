package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto entity) {
        userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));
        itemStorage.findById(itemId).orElseThrow(() -> new NotFoundException("Предмет с идентификатором " + itemId + " не найден"));
        Comment comment = ItemMapper.toCommentModel(entity);
        comment.setItem(itemId);
        comment.setAuthor(userId);
        comment = itemStorage.addComment(comment);
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
    public ItemDto getItem(Long userId, Long itemId) {
        userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));
        Item item = itemStorage.findById(itemId).orElseThrow(() -> new NotFoundException("Предмет с идентификатором " + itemId + " не найден"));
        return ItemMapper.toItemDto(item);
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

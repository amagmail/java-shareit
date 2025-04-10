package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto create(Long userId, ItemDto entity) {

        //User checkUser = userStorage.getReferenceById(10L);
        //Optional<User> maybeUser = userStorage.findById(10L);

        Optional<User> maybeUser = userStorage.findById(userId);
        if (maybeUser.isEmpty()) {
            throw new NotFoundException("Пользователь с идентификатором " + userId + " не найден");
        }
        Item item = ItemMapper.toItemModel(entity);
        item.setOwner(userId);
        item = itemStorage.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto entity) {

        //User checkUser = userStorage.getReferenceById(userId);
        //Optional<User> maybeUser = userStorage.findById(userId);

        Optional<User> maybeUser = userStorage.findById(userId);
        if (maybeUser.isEmpty()) {
            throw new NotFoundException("Пользователь с идентификатором " + userId + " не найден");
        }
        Item item = ItemMapper.toItemModel(entity);
        item.setOwner(userId);
        item.setId(itemId);
        item = itemStorage.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getItem(Long userId, Long itemId) {
        Optional<User> maybeUser = userStorage.findById(userId);
        if (maybeUser.isEmpty()) {
            throw new NotFoundException("Пользователь с идентификатором " + userId + " не найден");
        }
        Optional<Item> maybeItem = itemStorage.findById(itemId);
        if (maybeItem.isEmpty()) {
            throw new NotFoundException("Предмет с идентификатором " + itemId + " не найден");
        }
        Item item = maybeItem.get();
        return ItemMapper.toItemDto(item);
    }

    @Override
    public Collection<ItemDto> getItems(Long userId) {
        return itemStorage.findAll().stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public Collection<ItemDto> getSearch(Long userId, String text) {
        return itemStorage.getSearch(text).stream()
                .map(ItemMapper::toItemDto)
                .toList();

    }

}

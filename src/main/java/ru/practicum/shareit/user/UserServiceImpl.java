package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dal.UserRepository;


import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto entity) {
        if (userRepository.findByEmail(entity.getEmail()).isPresent()) {
            throw new DuplicateException("Пользователь с таким email уже существует");
        }
        User user = userRepository.save(UserMapper.toUserModel(entity));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto update(Long id, UserDto entity) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + id + " не найден"));
        if (entity.getName() != null) {
            user.setName(entity.getName());
        }
        if (entity.getEmail() != null) {
            Optional<User> userByEmail = userRepository.findByEmail(entity.getEmail());
            if (userByEmail.isPresent() && !userByEmail.get().getId().equals(user.getId())) {
                throw new DuplicateException("Пользователь с таким email уже существует");
            }
            user.setEmail(entity.getEmail());
        }
        userRepository.save(user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public void remove(Long id) {
        Optional<User> maybeUser = userRepository.findById(id);
        if (maybeUser.isEmpty()) {
            throw new NotFoundException("Пользователь с идентификатором " + id + " не найден");
        }
        User user = maybeUser.get();
        userRepository.delete(user);
    }

    @Override
    public UserDto getItem(Long id) {
        Optional<User> maybeUser = userRepository.findById(id);
        if (maybeUser.isEmpty()) {
            throw new NotFoundException("Пользователь с идентификатором " + id + " не найден");
        }
        User user = maybeUser.get();
        return UserMapper.toUserDto(user);
    }

    @Override
    public Collection<UserDto> getItems() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

}

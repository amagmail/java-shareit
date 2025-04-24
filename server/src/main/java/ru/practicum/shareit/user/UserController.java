package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto create(@RequestBody UserDto entity) {
        return userService.create(entity);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable("id") Long id, @RequestBody UserDto entity) {
        return userService.update(id, entity);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable("id") Long id) {
        userService.remove(id);
    }

    @GetMapping("/{id}")
    public UserDto getItem(@PathVariable("id") Long id) {
        return userService.getItem(id);
    }

    @GetMapping
    public Collection<UserDto> getItems() {
        return userService.getItems();
    }

}

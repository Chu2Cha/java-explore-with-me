package ru.practicum.explore_with_me.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.user.UserDto;
import ru.practicum.explore_with_me.service.admin.service.user.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@Slf4j
public class UsersController {
    private final UserService userService;


    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam long[] ids,
                                     @RequestParam(defaultValue = "0") int from,
                                     @RequestParam(defaultValue = "10") int size) {
        log.info("get all users in list {}, from {}, size = {}", ids, from, size);
        return userService.getAllUsers(ids, from, size);
    }
    @PostMapping
    public UserDto postUser(@RequestBody UserDto userDto) {
        log.info("post userDto:" + userDto);
        return userService.postUser(userDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        log.info("delete user id = " + id);
        userService.deleteUser(id);
    }


}

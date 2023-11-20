package ru.practicum.explore_with_me.controller.admin_c;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.user.NewUserRequest;
import ru.practicum.explore_with_me.dto.user.UserDto;
import ru.practicum.explore_with_me.service.admin_s.user.AdminUserService;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@Slf4j
public class AdminUserController {
    private final AdminUserService userService;


    @Autowired
    public AdminUserController(AdminUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public @ResponseStatus(HttpStatus.OK) List<UserDto> getAllUsers(@RequestParam long[] ids,
                                     @RequestParam(defaultValue = "0") int from,
                                     @RequestParam(defaultValue = "10") int size) {
        log.info("get all users in list {}, from {}, size = {}", ids, from, size);
        return userService.getAllUsers(ids, from, size);
    }
    @PostMapping
    public @ResponseStatus(HttpStatus.CREATED) UserDto postUser(@RequestBody NewUserRequest newUserRequest) {
        log.info("post newUserRequest:" + newUserRequest);
        return userService.postUser(newUserRequest);
    }

    @DeleteMapping("/{id}")
    public @ResponseStatus(HttpStatus.NO_CONTENT) void delete(@PathVariable("id") long id) {
        log.info("delete user id = " + id);
        userService.deleteUser(id);
    }


}

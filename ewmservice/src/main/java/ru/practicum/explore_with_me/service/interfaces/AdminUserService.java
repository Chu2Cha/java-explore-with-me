package ru.practicum.explore_with_me.service.interfaces;

import ru.practicum.explore_with_me.dto.user.NewUserRequest;
import ru.practicum.explore_with_me.dto.user.UserDto;

import java.util.List;

public interface AdminUserService {
    List<UserDto> getAllUsers(Long[]ids, int from, int size);

    UserDto postUser (NewUserRequest userDto);

    void deleteUser(long id);


}

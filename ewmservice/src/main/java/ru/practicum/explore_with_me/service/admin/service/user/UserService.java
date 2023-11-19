package ru.practicum.explore_with_me.service.admin.service.user;

import ru.practicum.explore_with_me.dto.user.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers(long[]ids, int from, int size);

    UserDto postUser (UserDto userDto);

    void deleteUser(long id);


}

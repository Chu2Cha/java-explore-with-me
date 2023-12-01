package ru.practicum.explore_with_me.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.user.NewUserRequest;
import ru.practicum.explore_with_me.dto.user.UserDto;
import ru.practicum.explore_with_me.dto.user.UserShortDto;
import ru.practicum.explore_with_me.model.User;

@Component
public class UserMapper {

    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getEmail(),
                user.getId(),
                user.getName()

        );
    }

    public User toUserFromNew(NewUserRequest newUserRequest) {
        User user = new User();
        user.setEmail(newUserRequest.getEmail());
        user.setName(newUserRequest.getName());
        return user;
    }

    public UserShortDto toUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }

}

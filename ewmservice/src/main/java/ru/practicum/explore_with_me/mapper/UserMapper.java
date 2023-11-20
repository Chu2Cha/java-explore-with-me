package ru.practicum.explore_with_me.mapper;

import ru.practicum.explore_with_me.dto.user.NewUserRequest;
import ru.practicum.explore_with_me.dto.user.UserDto;
import ru.practicum.explore_with_me.model.User;

public class UserMapper {

    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getEmail(),
                user.getId(),
                user.getName()

        );
    }

    public User toUser(UserDto userDto) {
        return new User(
                userDto.getEmail(),
                userDto.getId(),
                userDto.getName()
        );
    }

    public User toUserFromNew(NewUserRequest newUserRequest){
        User user = new User();
        user.setEmail(newUserRequest.getEmail());
        user.setName(newUserRequest.getName());
        return user;
    }

}

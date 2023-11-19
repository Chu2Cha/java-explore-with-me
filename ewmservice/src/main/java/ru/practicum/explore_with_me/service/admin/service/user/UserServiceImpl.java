package ru.practicum.explore_with_me.service.admin.service.user;

import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.user.UserDto;
import ru.practicum.explore_with_me.exceptions.NotFoundException;
import ru.practicum.explore_with_me.mapper.UserMapper;
import ru.practicum.explore_with_me.repository.admin.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper = new UserMapper();


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getAllUsers(long[] ids, int from, int size) {
        return userRepository.findAllByIdIn(ids)
                .stream()
                .skip(from)
                .limit(size)
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto postUser(UserDto userDto) {
        return userMapper.toUserDto(userRepository.save(userMapper.toUser(userDto)));

    }

    @Override
    public void deleteUser(long id) {
        findUserById(id);
        userRepository.deleteById(id);

    }

    private UserDto findUserById(long id) {
        return userMapper.toUserDto(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден.")));
    }
}

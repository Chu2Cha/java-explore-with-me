package ru.practicum.explore_with_me.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.user.NewUserRequest;
import ru.practicum.explore_with_me.dto.user.UserDto;
import ru.practicum.explore_with_me.exceptions.ConflictException;
import ru.practicum.explore_with_me.exceptions.NotFoundException;
import ru.practicum.explore_with_me.mapper.UserMapper;
import ru.practicum.explore_with_me.model.User;
import ru.practicum.explore_with_me.repository.UserRepository;
import ru.practicum.explore_with_me.service.interfaces.AdminUserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper = new UserMapper();


    public AdminUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getAllUsers(Long[] ids, int from, int size) {
        if(ids == null || ids.length ==0 ){
            return userRepository.findAll()
                    .stream()
                    .skip(from)
                    .limit(size)
                    .map(userMapper::toUserDto)
                    .collect(Collectors.toList());
        }
        return userRepository.findAllByIdIn(ids)
                .stream()
                .skip(from)
                .limit(size)
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto postUser(NewUserRequest newUserRequest) {
        checkUserForBusyName(newUserRequest.getName());
        checkUserForBusyEmail(newUserRequest.getEmail());
        User user = userMapper.toUserFromNew(newUserRequest);
        return userMapper.toUserDto(userRepository.save(user));
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

    private void checkUserForBusyName(String name){
        if(!userRepository.findOneByName(name).isEmpty()){
            throw new ConflictException("Ошибка: имя пользователя " + name + " уже занято.");
        }
    }

    private void checkUserForBusyEmail(String email){
        if(!userRepository.findOneByEmail(email).isEmpty()){
            throw new ConflictException("Ошибка: электронная почта " + email + " уже занята.");
        }
    }
}

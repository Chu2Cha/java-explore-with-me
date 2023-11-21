package ru.practicum.explore_with_me.service.private_s.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.EventState;
import ru.practicum.explore_with_me.dto.event.NewEventDto;
import ru.practicum.explore_with_me.exceptions.NotFoundException;
import ru.practicum.explore_with_me.mapper.EventMapper;
import ru.practicum.explore_with_me.model.Category;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.User;
import ru.practicum.explore_with_me.repository.CategoryRepository;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@Slf4j
@AllArgsConstructor

public class PrivateEventServiceImpl implements PrivateEventService{
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;

    @Override
    public EventFullDto postNewEvent(Long userId, NewEventDto newEventDto, LocalDateTime publishedOn) {
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Категория id =" + newEventDto.getCategory() + " не найдена."));
        Long confirmedRequests = 0L;
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь =" + userId + " не найден."));
        EventState state = EventState.PENDING;
        Long veiws = 0L;
        Event event = eventMapper.toEventFromNew(newEventDto,category,
                confirmedRequests,publishedOn,initiator,state,veiws);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    private Long findConfirmedRequests(NewEventDto newEventDto) {
        return 0L;
    }

    @Override
    public EventFullDto getEvent(Long userId, Long id) {
        return null;
    }
}

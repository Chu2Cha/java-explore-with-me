package ru.practicum.explore_with_me.service.private_s.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.enums.EventState;
import ru.practicum.explore_with_me.dto.event.*;
import ru.practicum.explore_with_me.mapper.CategoryMapper;
import ru.practicum.explore_with_me.mapper.EventMapper;
import ru.practicum.explore_with_me.mapper.UserMapper;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.repository.CategoryRepository;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.repository.UserRepository;
import ru.practicum.explore_with_me.service.EventValidation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class PrivateEventServiceImpl extends EventValidation implements PrivateEventService {

    private final EventMapper eventMapper = new EventMapper(new CategoryMapper(), new UserMapper());

    public PrivateEventServiceImpl(EventRepository eventRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        super(eventRepository, userRepository, categoryRepository);
    }

    @Override
    public EventFullDto postNewEvent(Long userId, NewEventDto newEventDto, LocalDateTime cratedOn) {
        Event event = eventMapper.toEventFromNew(newEventDto);
        event.setCreatedOn(cratedOn);
        checkDateValidation(event, cratedOn, 2);
        event.setCategory(getCategory(newEventDto.getCategory()));
        Long confirmedRequests = 0L;
        event.setConfirmedRequests(confirmedRequests);
        event.setInitiator(getInitiator(userId));
        event.setState(EventState.PENDING);
        Long views = 0L;
        event.setViews(views);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }


    @Override
    public EventFullDto getUserEvent(Long userId, Long id) {
        return eventMapper.toEventFullDto(findInitiatorsEvent(userId, id));
    }

    @Override
    public List<EventShortDto> getAllUserEvents(Long userId, int from, int size) {
        return eventRepository.findAllByInitiatorId(userId).stream()
                .skip(from)
                .limit(size)
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateUserEvent(Long userId, Long id, UpdateEventUserRequest updateEventUserRequest,
                                        LocalDateTime cratedOn) {
        Event event = findInitiatorsEvent(userId, id);
        if (updateEventUserRequest.getEventDate() != null) {
            event.setEventDate(updateEventUserRequest.getEventDate());
        }
        event.setCreatedOn(cratedOn);
        checkDateValidation(event, cratedOn, 2);

        updateEvent(updateEventUserRequest, event);
        if(updateEventUserRequest.getStateAction()!=null){
            switch (updateEventUserRequest.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
            }
        }
        log.info("user updated event: " + event);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

}

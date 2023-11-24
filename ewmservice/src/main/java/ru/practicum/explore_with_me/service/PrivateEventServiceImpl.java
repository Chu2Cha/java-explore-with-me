package ru.practicum.explore_with_me.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.enums.EventState;
import ru.practicum.explore_with_me.dto.event.*;
import ru.practicum.explore_with_me.mapper.EventMapper;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.service.interfaces.PrivateEventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@AllArgsConstructor
public class PrivateEventServiceImpl implements PrivateEventService {

    private final EventMapper eventMapper;
    private final EventValidation eventValidation;
    private final EventRepository eventRepository;

    @Override
    public EventFullDto postNewEvent(Long userId, NewEventDto newEventDto, LocalDateTime cratedOn) {
        Event event = eventMapper.toEventFromNew(newEventDto);
        event.setCreatedOn(cratedOn);
        eventValidation.checkDateValidation(event, cratedOn, 2);
        event.setCategory(eventValidation.getCategory(newEventDto.getCategory()));
        Long confirmedRequests = 0L;
        event.setConfirmedRequests(confirmedRequests);
        event.setInitiator(eventValidation.getInitiator(userId));
        event.setState(EventState.PENDING);
        Long views = 0L;
        event.setViews(views);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }


    @Override
    public EventFullDto getUserEvent(Long userId, Long id) {
        return eventMapper.toEventFullDto(eventValidation.findInitiatorsEvent(userId, id));
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
        Event event = eventValidation.findInitiatorsEvent(userId, id);
        if (updateEventUserRequest.getEventDate() != null) {
            event.setEventDate(updateEventUserRequest.getEventDate());
        }
        event.setCreatedOn(cratedOn);
        eventValidation.checkDateValidation(event, cratedOn, 2);

        eventValidation.updateEvent(updateEventUserRequest, event);
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

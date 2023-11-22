package ru.practicum.explore_with_me.service.private_s.event;

import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.EventShortDto;
import ru.practicum.explore_with_me.dto.event.NewEventDto;
import ru.practicum.explore_with_me.dto.event.UpdateEventUserRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface PrivateEventService {
    EventFullDto postNewEvent(Long userId, NewEventDto newEventDto, LocalDateTime cratedOn);

    EventFullDto getUserEvent(Long userId, Long id);

    List<EventShortDto> getAllUserEvents(Long userId, int from, int size);

    EventFullDto updateUserEvent(Long userId, Long id, UpdateEventUserRequest updateEventUserRequest,
                                 LocalDateTime cratedOn);
}

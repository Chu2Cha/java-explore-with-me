package ru.practicum.explore_with_me.service.private_s.event;

import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.NewEventDto;

import java.time.LocalDateTime;

public interface PrivateEventService {
    EventFullDto postNewEvent(Long userId, NewEventDto newEventDto, LocalDateTime createdOn);

    EventFullDto getEvent(Long userId, Long id);
}

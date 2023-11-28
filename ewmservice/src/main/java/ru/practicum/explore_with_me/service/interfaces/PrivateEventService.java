package ru.practicum.explore_with_me.service.interfaces;

import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.EventShortDto;
import ru.practicum.explore_with_me.dto.event.NewEventDto;
import ru.practicum.explore_with_me.dto.event.UpdateEventUserRequest;
import ru.practicum.explore_with_me.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.explore_with_me.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.explore_with_me.dto.request.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PrivateEventService {
    EventFullDto postNewEvent(Long userId, NewEventDto newEventDto, LocalDateTime cratedOn);

    EventFullDto getUserEvent(Long userId, Long id);

    List<EventShortDto> getAllUserEvents(Long userId, int from, int size);

    EventFullDto updateUserEvent(Long userId, Long id, UpdateEventUserRequest updateEventUserRequest,
                                 LocalDateTime cratedOn);

    List<ParticipationRequestDto> getEventRequests(Long userId, Long id);

    EventRequestStatusUpdateResult setEventRequestsStatus(Long userId, Long id, EventRequestStatusUpdateRequest requestStatusUpdateRequest);
}

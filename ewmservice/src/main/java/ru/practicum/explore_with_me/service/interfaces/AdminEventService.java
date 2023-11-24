package ru.practicum.explore_with_me.service.interfaces;

import ru.practicum.explore_with_me.dto.enums.EventState;
import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {
    EventFullDto pathEvent(Long id, UpdateEventAdminRequest updateEventAdminRequest, LocalDateTime publishedOn);

    List<EventFullDto> searchEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                    String rangeStart, String rangeEnd, int from, int size);
}

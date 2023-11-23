package ru.practicum.explore_with_me.service.public_s.event;

import ru.practicum.explore_with_me.dto.enums.SortEvents;
import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.EventShortDto;

import java.util.List;

public interface PublicEventService {
    EventFullDto getPublishedEvent(Long id);

    List<EventShortDto> searchEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                     String rangeEnd, Boolean onlyAvailable, SortEvents sort, int from, int size);
}

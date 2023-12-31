package ru.practicum.explore_with_me.service.interfaces;

import ru.practicum.explore_with_me.dto.enums.SortEvents;
import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PublicEventService {
    EventFullDto getPublishedEvent(Long id, HttpServletRequest request);

    List<EventShortDto> searchEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                     String rangeEnd, Boolean onlyAvailable, SortEvents sort, int from, int size,
                                     HttpServletRequest request);
}

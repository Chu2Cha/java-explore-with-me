package ru.practicum.explore_with_me.service.public_s.event;

import ru.practicum.explore_with_me.dto.event.EventFullDto;

public interface PublicEventService {
    EventFullDto getPublishedEvent(Long id);
}

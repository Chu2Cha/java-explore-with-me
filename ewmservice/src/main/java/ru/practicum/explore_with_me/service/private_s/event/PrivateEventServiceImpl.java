package ru.practicum.explore_with_me.service.private_s.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.NewEventDto;
import ru.practicum.explore_with_me.repository.EventRepository;

import java.time.LocalDateTime;

@Service
@Slf4j
public class PrivateEventServiceImpl implements PrivateEventService{
    private final EventRepository eventRepository;

    public PrivateEventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public EventFullDto postNewEvent(Long userId, NewEventDto newEventDto, LocalDateTime createdOn) {
        return null;
    }

    @Override
    public EventFullDto getEvent(Long userId, Long id) {
        return null;
    }
}

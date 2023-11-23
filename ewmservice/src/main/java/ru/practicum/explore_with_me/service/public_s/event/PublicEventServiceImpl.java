package ru.practicum.explore_with_me.service.public_s.event;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.enums.EventState;
import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.exceptions.NotFoundException;
import ru.practicum.explore_with_me.mapper.EventMapper;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.repository.EventRepository;

@Service
@AllArgsConstructor
public class PublicEventServiceImpl implements PublicEventService{
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    public EventFullDto getPublishedEvent(Long id) {
        Event event = eventRepository.findByIdAndStateIs(id, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Event id = " + id + " is not found."));
        return eventMapper.toEventFullDto(event);
    }
}

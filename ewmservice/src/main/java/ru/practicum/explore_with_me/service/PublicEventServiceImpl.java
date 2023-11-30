package ru.practicum.explore_with_me.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.enums.EventState;
import ru.practicum.explore_with_me.dto.enums.SortEvents;
import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.EventShortDto;
import ru.practicum.explore_with_me.exceptions.BadRequestException;
import ru.practicum.explore_with_me.exceptions.NotFoundException;
import ru.practicum.explore_with_me.mapper.EventMapper;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.service.interfaces.PublicEventService;
import statclient.StatisticsClient;
import statdto.dto.EndpointHitDto;


import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventValidation eventValidation;
    private final StatisticsClient statisticsClient;

    @Override
    public EventFullDto getPublishedEvent(Long id, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndStateIs(id, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Event id = " + id + " is not found."));
        event.setViews(event.getViews() + 1);
        postEndpointHit(request);
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> searchEvents(String text, List<Long> categories, Boolean paid,
                                            String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                            SortEvents sort, int from, int size, HttpServletRequest request) {
        LocalDateTime startDate = eventValidation.stringStartToDate(rangeStart);
        LocalDateTime endDate = eventValidation.stringEndToDate(rangeEnd);
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new BadRequestException("дата начала выборки должна быть позже даты конца выборки");
        }
        PageRequest page = PageRequest.of(from / size, size);
        List<Event> eventList;
        EventState state = EventState.PUBLISHED;
        if (sort != null) {
            switch (sort) {
                case EVENT_DATE:
                    eventList = eventRepository.publicSearchEventsByEventDate(text, categories, paid, startDate, endDate, state, page);
                    break;
                case VIEWS:
                    eventList = eventRepository.publicSearchEventsByViews(text, categories, paid, startDate, endDate, state, page);
                    break;
                default:
                    throw new BadRequestException("В запросе указан некорректный способ сортировки.");
            }
        } else {
            eventList = eventRepository.publicSearchEventsWithoutSort(text, categories, paid, startDate, endDate, state, page);
        }
        postEndpointHit(request);
        return eventList.stream().map(eventMapper::toEventShortDto).collect(Collectors.toList());
    }

    private void postEndpointHit(HttpServletRequest request) {
        EndpointHitDto endpointHit = EndpointHitDto.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app("ewm-main-service")
                .timestamp(LocalDateTime.now())
                .build();
        statisticsClient.postHit(endpointHit);
    }
}

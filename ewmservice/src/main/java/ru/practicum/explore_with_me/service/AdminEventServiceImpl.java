package ru.practicum.explore_with_me.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.enums.EventState;
import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.UpdateEventAdminRequest;
import ru.practicum.explore_with_me.exceptions.BadRequestException;
import ru.practicum.explore_with_me.exceptions.ConflictException;
import ru.practicum.explore_with_me.mapper.EventMapper;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.service.interfaces.AdminEventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {
    private final EventMapper eventMapper;
    private final EventValidation eventValidation;
    private final EventRepository eventRepository;


    @Override
    public EventFullDto pathEvent(Long id, UpdateEventAdminRequest updateEventAdminRequest, LocalDateTime publishedOn) {
        Event event = eventValidation.findEvent(id);
        eventValidation.updateEvent(updateEventAdminRequest, event);
        if (updateEventAdminRequest.getStateAction() == null) {
            eventValidation.checkDateValidation(event, LocalDateTime.now(), 2); //статуса нет, проверка даты события
        } else {
            switch (updateEventAdminRequest.getStateAction()) {
                case PUBLISH_EVENT:
                    if (!event.getState().equals(EventState.PENDING)) {
                        throw new ConflictException("событие можно публиковать, " +
                                "только если оно в состоянии ожидания публикации");
                    }
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(publishedOn);
                    eventValidation.checkDateValidation(event, publishedOn, 1); // проверка даты события после публикации
                    break;
                case REJECT_EVENT:
                    eventValidation.checkDateValidation(event, LocalDateTime.now(), 2);//публикации нет, проверка даты события
                    event.setState(EventState.CANCELED);
                    break;
            }
        }
        log.info("admin updated event: " + event);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventFullDto> searchEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                           String rangeStart, String rangeEnd, int from, int size) {
        LocalDateTime startDate = eventValidation.stringStartToDate(rangeStart);
        LocalDateTime endDate = eventValidation.stringEndToDate(rangeEnd);
        if(startDate.isAfter(endDate)){
            throw new BadRequestException("дата начала выборки должна быть позже даты конца выборки");
        }
        PageRequest page = PageRequest.of(from / size, size);
        List<Event> eventList =  eventRepository.adminSearchEvents(users, states, categories, startDate, endDate, page);
        return eventList.stream()
                .map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }



}

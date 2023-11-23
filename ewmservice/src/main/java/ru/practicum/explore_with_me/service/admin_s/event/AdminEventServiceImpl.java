package ru.practicum.explore_with_me.service.admin_s.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.enums.EventState;
import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.UpdateEventAdminRequest;
import ru.practicum.explore_with_me.exceptions.ConflictException;
import ru.practicum.explore_with_me.mapper.CategoryMapper;
import ru.practicum.explore_with_me.mapper.EventMapper;
import ru.practicum.explore_with_me.mapper.UserMapper;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.repository.CategoryRepository;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.repository.UserRepository;
import ru.practicum.explore_with_me.service.EventValidation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminEventServiceImpl extends EventValidation implements AdminEventService {
    private final EventMapper eventMapper = new EventMapper(new CategoryMapper(), new UserMapper());

    public AdminEventServiceImpl(EventRepository eventRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        super(eventRepository, userRepository, categoryRepository);
    }

    @Override
    public EventFullDto pathEvent(Long id, UpdateEventAdminRequest updateEventAdminRequest, LocalDateTime publishedOn) {
        Event event = findEvent(id);
        updateEvent(updateEventAdminRequest, event);
        if (updateEventAdminRequest.getStateAction() == null) {
            checkDateValidation(event, LocalDateTime.now(), 2); //статуса нет, проверка даты события
        } else {
            switch (updateEventAdminRequest.getStateAction()) {
                case PUBLISH_EVENT:
                    if (!event.getState().equals(EventState.PENDING)) {
                        throw new ConflictException("событие можно публиковать, " +
                                "только если оно в состоянии ожидания публикации");
                    }
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(publishedOn);
                    checkDateValidation(event, publishedOn, 1); // проверка даты события после публикации
                    break;
                case REJECT_EVENT:
                    checkDateValidation(event, LocalDateTime.now(), 2);//публикации нет, проверка даты события
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
        LocalDateTime startDate = stringToDate(rangeStart);
        LocalDateTime endDate = stringToDate(rangeEnd);
        PageRequest page = PageRequest.of(from / size, size);
        List<Event> eventList =  eventRepository.adminSearchEvents(users, states, categories, startDate, endDate, page);
        return eventList.stream()
                .map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    private LocalDateTime stringToDate(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime daate = null;
        if (stringDate != null) {
            daate = LocalDateTime.parse(stringDate, formatter);
        }
        return daate;
    }

}

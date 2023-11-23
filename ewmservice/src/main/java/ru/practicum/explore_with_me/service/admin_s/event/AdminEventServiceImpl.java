package ru.practicum.explore_with_me.service.admin_s.event;

import lombok.extern.slf4j.Slf4j;
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

}

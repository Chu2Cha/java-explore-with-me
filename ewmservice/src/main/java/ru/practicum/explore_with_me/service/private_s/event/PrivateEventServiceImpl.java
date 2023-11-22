package ru.practicum.explore_with_me.service.private_s.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.event.*;
import ru.practicum.explore_with_me.exceptions.BadRequestException;
import ru.practicum.explore_with_me.exceptions.NotFoundException;
import ru.practicum.explore_with_me.mapper.EventMapper;
import ru.practicum.explore_with_me.model.Category;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.User;
import ru.practicum.explore_with_me.repository.CategoryRepository;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor

public class PrivateEventServiceImpl implements PrivateEventService{
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;

    @Override
    public EventFullDto postNewEvent(Long userId, NewEventDto newEventDto, LocalDateTime cratedOn) {
        Event event = eventMapper.toEventFromNew(newEventDto);
        event.setCreatedOn(cratedOn);
        checkDateValidation(event, cratedOn, 2);
        event.setCategory(getCategory(newEventDto.getCategory()));
        Long confirmedRequests = 0L;
        event.setConfirmedRequests(confirmedRequests);
        event.setInitiator(getInitiator(userId));
        event.setState(EventState.PENDING);
        Long views = 0L;
        event.setViews(views);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    private User getInitiator(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь =" + userId + " не найден."));
    }

    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория id =" + categoryId + " не найдена."));
    }

    private static void checkDateValidation(Event event, LocalDateTime cratedOn, int hours) {
        if(event.getEventDate().isBefore(cratedOn.plusHours(hours))){
            throw new BadRequestException("Дата и время, на которые намечено событие, " + event.getEventDate() +
                    " не может быть раньше, чем через " + hours + " час(а) от текущего момента " + cratedOn);
        }
    }

    @Override
    public EventFullDto getUserEvent(Long userId, Long id) {
        return eventMapper.toEventFullDto(findEvent(userId, id));
    }

    @Override
    public List<EventShortDto> getAllUserEvents(Long userId, int from, int size) {
        return eventRepository.findAllByInitiatorId(userId).stream()
                .skip(from)
                .limit(size)
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateUserEvent(Long userId, Long id, UpdateEventUserRequest updateEventUserRequest,
                                        LocalDateTime cratedOn) {
        Event event = findEvent(userId, id);
        if(event.getState().equals(EventState.PUBLISHED)){
            throw new BadRequestException("изменить можно только отмененные события " +
                    "или события в состоянии ожидания модерации");
        }
        event.setCreatedOn(cratedOn);
        if(updateEventUserRequest.getAnnotation()!=null){
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }
        if(updateEventUserRequest.getCategory()!=null){
            event.setCategory(getCategory(updateEventUserRequest.getCategory()));
        }
        if(updateEventUserRequest.getDescription()!=null){
            event.setDescription(updateEventUserRequest.getDescription());
        }
        if(updateEventUserRequest.getEventDate()!=null){
            event.setEventDate(updateEventUserRequest.getEventDate());
        }
        checkDateValidation(event, cratedOn, 2);
        if(updateEventUserRequest.getLocation()!=null){
            event.setLocation(updateEventUserRequest.getLocation());
        }
       if(updateEventUserRequest.getPaid() != null){  //почему isPaid? у меня нет такой переменной!
           event.setPaid(updateEventUserRequest.getPaid());
       }
       if(updateEventUserRequest.getParticipantLimit()!=null){
           event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
       }
       if(updateEventUserRequest.getRequestModeration()!=null){
           event.setRequestModeration(updateEventUserRequest.getRequestModeration());
       }
       if(updateEventUserRequest.getStateAction() == StateAction.SEND_TO_REVIEW){
           event.setState(EventState.PENDING);
       }
       if(updateEventUserRequest.getStateAction() == StateAction.CANCEL_REVIEW){
           event.setState(EventState.CANCELED);
       }
       if(updateEventUserRequest.getTitle()!= null){
           event.setTitle(updateEventUserRequest.getTitle());
       }
        return eventMapper.toEventFullDto(eventRepository.save(event));

    }

    private Event findEvent(Long userId, Long id) {
        return eventRepository.findByIdAndInitiatorId(id, userId)
                .orElseThrow(() -> new NotFoundException("У пользователя id = " + userId + " нет эвента id = " + id));
    }
}

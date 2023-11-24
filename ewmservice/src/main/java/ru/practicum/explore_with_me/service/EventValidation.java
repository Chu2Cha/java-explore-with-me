package ru.practicum.explore_with_me.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.enums.EventState;
import ru.practicum.explore_with_me.dto.event.UpdateEventRequest;
import ru.practicum.explore_with_me.exceptions.BadRequestException;
import ru.practicum.explore_with_me.exceptions.ConflictException;
import ru.practicum.explore_with_me.exceptions.NotFoundException;
import ru.practicum.explore_with_me.model.Category;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.User;
import ru.practicum.explore_with_me.repository.CategoryRepository;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@AllArgsConstructor
public class EventValidation {
    protected final EventRepository eventRepository;
    protected final UserRepository userRepository;
    protected final CategoryRepository categoryRepository;

    protected User getInitiator(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь =" + userId + " не найден."));
    }

    protected Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория id =" + categoryId + " не найдена."));
    }

    protected void checkDateValidation(Event event, LocalDateTime cratedOn, int hours) {
        if(event.getEventDate().isBefore(cratedOn.plusHours(hours))){
            throw new BadRequestException("Дата и время, на которые намечено событие, " + event.getEventDate() +
                    " не может быть раньше, чем через " + hours + " час(а) от текущего момента " + cratedOn);
        }
    }

    protected Event findInitiatorsEvent(Long userId, Long id) {
        return eventRepository.findByIdAndInitiatorId(id, userId)
                .orElseThrow(() -> new NotFoundException("У пользователя id = " + userId + " нет эвента id = " + id));
    }

    protected Event findEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event id = " + id + "is not found."));
    }

    protected void updateEvent(UpdateEventRequest updateEventRequest, Event event) {
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("изменить можно только отмененные события " +
                    "или события в состоянии ожидания модерации");
        }
        if (updateEventRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getCategory() != null) {
            event.setCategory(getCategory(updateEventRequest.getCategory()));
        }
        if (updateEventRequest.getDescription() != null) {
            event.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getEventDate() != null) {
            event.setEventDate(updateEventRequest.getEventDate());
        }

        if (updateEventRequest.getLocation() != null) {
            event.setLocation(updateEventRequest.getLocation());
        }
        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventRequest.getRequestModeration());
        }
        if (updateEventRequest.getTitle() != null) {
            event.setTitle(updateEventRequest.getTitle());
        }
    }

    protected LocalDateTime stringToDate(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime daate = null;
        if (stringDate != null) {
            daate = LocalDateTime.parse(stringDate, formatter);
        }
        return daate;
    }
}

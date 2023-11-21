package ru.practicum.explore_with_me.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.EventState;
import ru.practicum.explore_with_me.dto.event.NewEventDto;
import ru.practicum.explore_with_me.model.Category;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.User;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class EventMapper {
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;

    public Event toEventFromNew(NewEventDto newEventDto, Category category, Long confirmedRequests,
                                LocalDateTime publishedOn, User initiator, EventState state, Long views){
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .confirmedRequests(confirmedRequests)
                .createdOn(newEventDto.getEventDate())
                .description(newEventDto.getDescription())
                .initiator(initiator)
                .location(newEventDto.getLocation())
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .publishedOn(publishedOn)
                .requestModeration(newEventDto.isRequestModeration())
                .state(state)
                .title(newEventDto.getTitle())
                .views(views)
                .build();
    }

    public EventFullDto toEventFullDto (Event event){
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(categoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getCreatedOn())
                .description(event.getDescription())
                .id(event.getId())
                .initiator(userMapper.toUserShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .createdOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

}

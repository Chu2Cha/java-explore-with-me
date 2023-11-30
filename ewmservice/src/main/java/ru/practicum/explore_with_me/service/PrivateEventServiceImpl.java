package ru.practicum.explore_with_me.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.enums.EventState;
import ru.practicum.explore_with_me.dto.enums.RequestStatus;
import ru.practicum.explore_with_me.dto.event.*;
import ru.practicum.explore_with_me.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.explore_with_me.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.explore_with_me.dto.request.ParticipationRequestDto;
import ru.practicum.explore_with_me.exceptions.ConflictException;
import ru.practicum.explore_with_me.mapper.EventMapper;
import ru.practicum.explore_with_me.mapper.RequestMapper;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.Request;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.repository.RequestRepository;
import ru.practicum.explore_with_me.service.interfaces.PrivateEventService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@AllArgsConstructor
public class PrivateEventServiceImpl implements PrivateEventService {

    private final EventMapper eventMapper;
    private final EventValidation eventValidation;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    @Override
    public EventFullDto postNewEvent(Long userId, NewEventDto newEventDto, LocalDateTime cratedOn) {
        Event event = eventMapper.toEventFromNew(newEventDto);
        event.setCreatedOn(cratedOn);
        eventValidation.checkDateValidation(event, cratedOn, 2);
        event.setCategory(eventValidation.getCategory(newEventDto.getCategory()));
        Long confirmedRequests = 0L;
        event.setConfirmedRequests(confirmedRequests);
        event.setInitiator(eventValidation.getInitiator(userId));
        event.setState(EventState.PENDING);
        Long views = 0L;
        event.setViews(views);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getUserEvent(Long userId, Long id) {
        return eventMapper.toEventFullDto(eventValidation.findInitiatorsEvent(userId, id));
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
        Event event = eventValidation.findInitiatorsEvent(userId, id);
        if (updateEventUserRequest.getEventDate() != null) {
            event.setEventDate(updateEventUserRequest.getEventDate());
        }
        event.setCreatedOn(cratedOn);
        eventValidation.checkDateValidation(event, cratedOn, 2);

        eventValidation.updateEvent(updateEventUserRequest, event);
        if (updateEventUserRequest.getStateAction() != null) {
            switch (updateEventUserRequest.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
            }
        }
        log.info("user updated event: " + event);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long id) {
        Event event = eventValidation.findInitiatorsEvent(userId, id);
        return requestRepository.findAllByEventId(event.getId()).stream()
                .map(requestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult setEventRequestsStatus(Long userId,
                                                                 Long id,
                                                                 EventRequestStatusUpdateRequest requestStatusUpdateRequest) {
        Event event = eventValidation.findInitiatorsEvent(userId, id);
        EventRequestStatusUpdateResult statusUpdateResult = new EventRequestStatusUpdateResult();
        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("нельзя подтвердить заявку, " +
                    "если уже достигнут лимит по заявкам на данное событие ");
        }
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        for (long requestId : requestStatusUpdateRequest.getRequestIds()) {

            Request request = requestRepository.findById(requestId).orElseThrow();
            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                throw new ConflictException("Статус заявки не PENDING");
            }
            RequestStatus status = requestStatusUpdateRequest.getStatus();

            if ((long) event.getParticipantLimit() == event.getConfirmedRequests()) {
                requestRepository.updateStatus(request.getId(), RequestStatus.REJECTED);
                rejectedRequests.add(requestMapper.toRequestDto(request));
            } else {
                requestRepository.updateStatus(request.getId(), status);
                request.setStatus(status);
                if (status.equals(RequestStatus.CONFIRMED)) {
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    confirmedRequests.add(requestMapper.toRequestDto(request));
                }
                if (status.equals(RequestStatus.REJECTED)) {
                    rejectedRequests.add(requestMapper.toRequestDto(request));
                }
            }
        }

        statusUpdateResult.setConfirmedRequests(confirmedRequests);
        statusUpdateResult.setRejectedRequests(rejectedRequests);

        return statusUpdateResult;
    }
}

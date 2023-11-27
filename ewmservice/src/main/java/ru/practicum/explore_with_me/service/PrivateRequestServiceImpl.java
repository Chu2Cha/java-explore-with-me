package ru.practicum.explore_with_me.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.enums.EventState;
import ru.practicum.explore_with_me.dto.enums.RequestStatus;
import ru.practicum.explore_with_me.dto.request.ParticipationRequestDto;
import ru.practicum.explore_with_me.exceptions.ConflictException;
import ru.practicum.explore_with_me.exceptions.NotFoundException;
import ru.practicum.explore_with_me.mapper.RequestMapper;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.Request;
import ru.practicum.explore_with_me.model.User;
import ru.practicum.explore_with_me.repository.RequestRepository;
import ru.practicum.explore_with_me.repository.UserRepository;
import ru.practicum.explore_with_me.service.interfaces.PrivateRequestService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class PrivateRequestServiceImpl implements PrivateRequestService {
    private final RequestRepository requestRepository;
    private final EventValidation eventValidation;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;

    @Override
    public ParticipationRequestDto postRequest(Long userId, Long eventId, LocalDateTime created) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User id = " + userId + "is not found."));
        Event event = eventValidation.findEvent(eventId);
        if(!requestRepository.findOneByRequesterIdAndEventId(userId,eventId).isEmpty()){
            throw new ConflictException("нельзя добавить повторный запрос");
        }
        if (event.getInitiator().getId() == userId) {
            throw new ConflictException("инициатор события не может добавить запрос на участие в своём событии");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("нельзя участвовать в неопубликованном событии");
        }
        if (event.getParticipantLimit() == 0 || event.getConfirmedRequests() < event.getParticipantLimit()) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }
        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("у события достигнут лимит запросов на участие");
        }
        Request newRequest = Request.builder()
                .created(created)
                .event(event)
                .requester(requester)
                .status(RequestStatus.PENDING)
                .build();
        newRequest = requestRepository.save(newRequest);
        return requestMapper.toRequestDto(newRequest);
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        return requestRepository.findAllByRequesterId(userId).stream()
                .map(requestMapper::toRequestDto)
                .collect(Collectors.toList());
    }
}

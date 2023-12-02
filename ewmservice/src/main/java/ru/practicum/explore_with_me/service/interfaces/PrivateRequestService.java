package ru.practicum.explore_with_me.service.interfaces;

import ru.practicum.explore_with_me.dto.request.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PrivateRequestService {
    ParticipationRequestDto postRequest(Long userId, Long eventId, LocalDateTime created);

    List<ParticipationRequestDto> getUserRequests(Long userId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}

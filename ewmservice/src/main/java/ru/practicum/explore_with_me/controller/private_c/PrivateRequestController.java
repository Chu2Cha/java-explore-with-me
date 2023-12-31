package ru.practicum.explore_with_me.controller.private_c;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.request.ParticipationRequestDto;
import ru.practicum.explore_with_me.service.interfaces.PrivateRequestService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@Slf4j
@AllArgsConstructor
public class PrivateRequestController {

    private final PrivateRequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto postRequest(
            @PathVariable Long userId,
            @RequestParam Long eventId) {
        LocalDateTime created = LocalDateTime.now();
        log.info("Created request: userid = {}, eventId = {}, created time = {}", userId, eventId, created);
        return requestService.postRequest(userId, eventId, created);

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getUserRequests(
            @PathVariable Long userId) {
        log.info("Get all requests from userid = {}", userId);
        return requestService.getUserRequests(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto cancelRequest(
            @PathVariable Long userId,
            @PathVariable Long requestId) {
        log.info("Cancel request userid = {}, requestId = {}", userId, requestId);
        return requestService.cancelRequest(userId, requestId);
    }
}

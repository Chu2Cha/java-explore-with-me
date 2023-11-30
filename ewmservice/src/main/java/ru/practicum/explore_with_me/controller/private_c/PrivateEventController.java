package ru.practicum.explore_with_me.controller.private_c;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.EventShortDto;
import ru.practicum.explore_with_me.dto.event.NewEventDto;
import ru.practicum.explore_with_me.dto.event.UpdateEventUserRequest;
import ru.practicum.explore_with_me.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.explore_with_me.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.explore_with_me.dto.request.ParticipationRequestDto;
import ru.practicum.explore_with_me.service.interfaces.PrivateEventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@Slf4j
@Validated
public class PrivateEventController {

    private final PrivateEventService privateEventService;

    public PrivateEventController(PrivateEventService privateEventService) {
        this.privateEventService = privateEventService;
    }

    @PostMapping
    public @ResponseStatus(HttpStatus.CREATED) EventFullDto postNewEvent(
            @PathVariable Long userId,
            @Valid @RequestBody NewEventDto newEventDto) {
        LocalDateTime cratedOn = LocalDateTime.now();
        log.info("post new event, userid = {}, newEventDto = {}, crated on  {}", userId, newEventDto, cratedOn);
        return privateEventService.postNewEvent(userId, newEventDto, cratedOn);
    }

    @GetMapping("/{id}")
    public @ResponseStatus(HttpStatus.OK) EventFullDto getUserEvent(
            @PathVariable Long userId,
            @PathVariable Long id) {
        log.info("get one event, userid = {}, event id = {}", userId, id);
        return privateEventService.getUserEvent(userId, id);
    }

    @GetMapping
    public @ResponseStatus(HttpStatus.OK) List<EventShortDto> getAllUserEvents(
            @PathVariable Long userId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("get all events from userid = {}", userId);
        return privateEventService.getAllUserEvents(userId, from, size);
    }

    @PatchMapping("/{id}")
    public @ResponseStatus(HttpStatus.OK) EventFullDto updateEvent(
            @PathVariable Long userId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        LocalDateTime cratedOn = LocalDateTime.now();
        log.info("update event, userid = {}, event id = {}, updateEventUserRequest = {}, created on {}",
                userId, id, updateEventUserRequest, cratedOn);
        return privateEventService.updateUserEvent(userId, id, updateEventUserRequest, cratedOn);
    }

    @GetMapping("/{id}/requests")
    public @ResponseStatus(HttpStatus.OK) List<ParticipationRequestDto> getEventRequests(
            @PathVariable Long userId,
            @PathVariable Long id) {
        log.info("get all requests for userid = {}, eventId = {}", userId, id);
        return privateEventService.getEventRequests(userId, id);
    }

    @PatchMapping("/{id}/requests")
    public @ResponseStatus(HttpStatus.OK) EventRequestStatusUpdateResult setEventRequestsStatus(
            @PathVariable Long userId,
            @PathVariable Long id,
            @RequestBody EventRequestStatusUpdateRequest requestStatusUpdateRequest) {
        log.info("set status for requests for userid = {}, eventId = {}, new status = {}"
                , userId, id, requestStatusUpdateRequest);
        return privateEventService.setEventRequestsStatus(userId, id, requestStatusUpdateRequest);

    }

}

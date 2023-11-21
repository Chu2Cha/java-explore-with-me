package ru.practicum.explore_with_me.controller.private_c;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.NewEventDto;
import ru.practicum.explore_with_me.service.private_s.event.PrivateEventService;

import java.time.LocalDateTime;

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
            @RequestBody NewEventDto newEventDto) {
        LocalDateTime publishedOn = LocalDateTime.now();
        log.info("post new event, userid = {}, newEventDto = {}, published on  {}", userId, newEventDto, publishedOn);
        return privateEventService.postNewEvent(userId, newEventDto, publishedOn);
    }

    @GetMapping("/{id}")
    public @ResponseStatus(HttpStatus.OK) EventFullDto getEvent(
            @PathVariable Long userId,
            @PathVariable Long id) {
        log.info("post new event, userid = {}, event id = {}", userId, id);
        return privateEventService.getEvent(userId, id);
    }


}

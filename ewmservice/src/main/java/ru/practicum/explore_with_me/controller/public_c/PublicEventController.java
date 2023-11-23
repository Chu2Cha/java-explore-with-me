package ru.practicum.explore_with_me.controller.public_c;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.service.public_s.event.PublicEventService;

@RestController
@RequestMapping(path = "/events")
@Slf4j
@Validated
public class PublicEventController {
    private final PublicEventService publicEventService;

    public PublicEventController(PublicEventService publicEventService) {
        this.publicEventService = publicEventService;
    }

    @GetMapping("/{id}")
    public @ResponseStatus(HttpStatus.OK) EventFullDto getPublishedEvent(@PathVariable Long id){
        log.info("try to find event id = {}", id);
        return publicEventService.getPublishedEvent(id);
    }

}

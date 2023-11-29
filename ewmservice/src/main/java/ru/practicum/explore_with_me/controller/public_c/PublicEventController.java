package ru.practicum.explore_with_me.controller.public_c;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.enums.SortEvents;
import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.EventShortDto;
import ru.practicum.explore_with_me.service.interfaces.PublicEventService;

import java.util.List;

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
    public @ResponseStatus(HttpStatus.OK) EventFullDto getPublishedEvent(@PathVariable Long id) {
        log.info("try to find event id = {}", id);
        return publicEventService.getPublishedEvent(id);
    }

    @GetMapping
    public @ResponseStatus(HttpStatus.OK) List<EventShortDto> searchEvents(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "paid", required = false) Boolean paid,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(name = "sort", required = false) SortEvents sort,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("find events where text = {}, categories = {}, paid = {}, rangeStart = {}, rangeEnd = {}, " +
                        "onlyAvailable = {}, sort = {}, from = {}, size = {}", text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size);
        return publicEventService.searchEvents(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size);

    }

}
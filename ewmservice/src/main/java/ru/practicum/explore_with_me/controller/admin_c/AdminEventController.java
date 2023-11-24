package ru.practicum.explore_with_me.controller.admin_c;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.enums.EventState;
import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.UpdateEventAdminRequest;
import ru.practicum.explore_with_me.service.interfaces.AdminEventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@Slf4j
@Validated
public class AdminEventController {
    private final AdminEventService adminEventService;

    public AdminEventController(AdminEventService adminEventService) {
        this.adminEventService = adminEventService;
    }

    @PatchMapping("/{id}")
    public @ResponseStatus(HttpStatus.OK) EventFullDto pathEvent(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        LocalDateTime publishedOn = LocalDateTime.now();
        log.info("update event, event id = {}, updateEventAdminRequest = {}, update time =  {}",
                id, updateEventAdminRequest, publishedOn);
        return adminEventService.pathEvent(id, updateEventAdminRequest, publishedOn);
    }

    @GetMapping
    public @ResponseStatus(HttpStatus.OK) List<EventFullDto> searchEvents(
            @RequestParam(name = "users", required = false) List<Long> users,
            @RequestParam(name = "states", required = false) List<EventState> states,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd ,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("search events: users = {}, states = {}, categories = {}, rangeStart = {}, rangeEnd = {}," +
                " from = {}, size = {}.", users, states, categories, rangeStart, rangeEnd, from, size);
        return adminEventService.searchEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }
}

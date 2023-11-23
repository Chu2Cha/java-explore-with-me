package ru.practicum.explore_with_me.controller.admin_c;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.UpdateEventAdminRequest;
import ru.practicum.explore_with_me.service.admin_s.event.AdminEventService;

import javax.validation.Valid;
import java.time.LocalDateTime;

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
        return adminEventService.pathEvent(id,updateEventAdminRequest, publishedOn);
    }
}

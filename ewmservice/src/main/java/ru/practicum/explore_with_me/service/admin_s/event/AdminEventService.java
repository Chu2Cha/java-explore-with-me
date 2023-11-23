package ru.practicum.explore_with_me.service.admin_s.event;

import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.UpdateEventAdminRequest;

import java.time.LocalDateTime;

public interface AdminEventService {
    EventFullDto pathEvent(Long id, UpdateEventAdminRequest updateEventAdminRequest, LocalDateTime publishedOn);
}

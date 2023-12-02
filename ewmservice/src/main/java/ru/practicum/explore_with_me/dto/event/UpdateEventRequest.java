package ru.practicum.explore_with_me.dto.event;


import lombok.Data;
import ru.practicum.explore_with_me.model.Location;

import java.time.LocalDateTime;

@Data
public abstract class UpdateEventRequest {
    String annotation;

    Long category;

    String description;

    LocalDateTime eventDate;

    Location location;

    Boolean paid;

    Integer participantLimit;

    Boolean requestModeration;

    String title;

}

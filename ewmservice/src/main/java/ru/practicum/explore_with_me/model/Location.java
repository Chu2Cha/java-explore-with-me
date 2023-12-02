package ru.practicum.explore_with_me.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Embeddable
public class Location {
    @Column(name = "event_lat")
    private Float lat;

    @Column(name = "event_lon")
    private Float lon;
}

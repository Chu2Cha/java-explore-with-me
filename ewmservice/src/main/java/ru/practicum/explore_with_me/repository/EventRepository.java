package ru.practicum.explore_with_me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore_with_me.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}

package ru.practicum.explore_with_me.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explore_with_me.dto.enums.EventState;
import ru.practicum.explore_with_me.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByIdAndInitiatorId(Long id, Long userId);

    List<Event> findAllByInitiatorId(Long userId);

    List<Event> findOneByCategoryId(Long id);

    @Query("SELECT e FROM Event AS e " +
            "JOIN e.category AS c " +
            "JOIN e.initiator AS u " +
            "WHERE ((:users) IS NULL OR u.id IN :users) " +
            "AND ((:states) IS NULL OR e.state IN :states) " +
            "AND ((:categories) IS NULL OR c.id IN :categories) " +
            "AND (cast(:rangeStart as date) IS NULL OR e.eventDate > :rangeStart) " +
            "AND (cast(:rangeEnd as date) IS NULL OR e.eventDate < :rangeEnd)")
    List<Event> adminSearchEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                  LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest pageable);

    Optional<Event> findByIdAndStateIs(Long id, EventState state);
}

package ru.practicum.explore_with_me.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
            "AND (cast(:startDate as date) IS NULL OR e.eventDate > :startDate) " +
            "AND (cast(:endDate as date) IS NULL OR e.eventDate < :endDate)")
    List<Event> adminSearchEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                  LocalDateTime startDate, LocalDateTime endDate, PageRequest pageable);

    Optional<Event> findByIdAndStateIs(Long id, EventState state);



    @Query("SELECT e FROM Event AS e " +
            "JOIN e.category AS c " +
            "WHERE ((:text) IS NULL OR upper(e.annotation) like upper(concat('%', :text, '%')) " +
            "OR upper(e.description) like upper(concat('%', :text, '%'))) " +
            "AND ((:categories) IS NULL OR c.id IN :categories) " +
            "AND ((:paid) IS NULL OR e.paid IN :paid) " +
            "AND (cast(:startDate as date) IS NULL OR e.eventDate > :startDate) " +
            "AND (cast(:endDate as date) IS NULL OR e.eventDate < :endDate) " +
            "AND ((:state) IS NULL OR e.state IN :state) " +
            "ORDER BY e.eventDate")
    List<Event> publicSearchEventsByEventDate(String text, List<Long> categories, Boolean paid,
                                              LocalDateTime startDate, LocalDateTime endDate,
                                              EventState state, PageRequest page);

    @Query("SELECT e FROM Event AS e " +
            "JOIN e.category AS c " +
            "WHERE ((:text) IS NULL OR upper(e.annotation) like upper(concat('%', :text, '%')) " +
            "OR upper(e.description) like upper(concat('%', :text, '%'))) " +
            "AND ((:categories) IS NULL OR c.id IN :categories) " +
            "AND ((:paid) IS NULL OR e.paid IN :paid) " +
            "AND (cast(:startDate as date) IS NULL OR e.eventDate > :startDate) " +
            "AND (cast(:endDate as date) IS NULL OR e.eventDate < :endDate) " +
            "AND ((:state) IS NULL OR e.state IN :state) " +
            "ORDER BY e.views")
    List<Event> publicSearchEventsByViews(String text, List<Long> categories, Boolean paid,
                                          LocalDateTime startDate, LocalDateTime endDate,
                                          EventState state, PageRequest page);

    @Query("SELECT e FROM Event AS e " +
            "JOIN e.category AS c " +
            "WHERE ((:text) IS NULL OR upper(e.annotation) like upper(concat('%', :text, '%')) " +
            "OR upper(e.description) like upper(concat('%', :text, '%'))) " +
            "AND ((:categories) IS NULL OR c.id IN :categories) " +
            "AND ((:paid) IS NULL OR e.paid IN :paid) " +
            "AND (cast(:startDate as date) IS NULL OR e.eventDate > :startDate) " +
            "AND (cast(:endDate as date) IS NULL OR e.eventDate < :endDate) " +
            "AND ((:state) IS NULL OR e.state IN :state)")
    List<Event> publicSearchEventsWithoutSort(String text, List<Long> categories, Boolean paid,
                                              LocalDateTime startDate, LocalDateTime endDate,
                                              EventState state, PageRequest page);

    @Modifying
    @Query("UPDATE Event e SET e.confirmedRequests = e.confirmedRequests + 1 WHERE e.id = :eventId")
    void increaseConfirmedRequests(@Param("eventId") Long eventId);
}
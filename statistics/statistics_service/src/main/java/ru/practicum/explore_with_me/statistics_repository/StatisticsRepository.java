package ru.practicum.explore_with_me.statistics_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.explore_with_me.statistics_model.EndpointHit;
import ru.practicum.explore_with_me.statistics_model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<EndpointHit, Long> {

    List<EndpointHit> findAllByTimestampBetween(LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT NEW ru.practicum.explore_with_me.statistics_model.ViewStats(e.app, e.uri, COUNT(e.id)) " +
            "FROM EndpointHit e " +
            "WHERE " +
            "e.timestamp BETWEEN :start AND :end " +
            "AND e.uri IN :uris " +
            "GROUP BY e.app, e.uri")
    List<ViewStats> findByTimestampBetweenAndUriInGroupByAppAndUri(@Param("start") LocalDateTime start,
                                                                   @Param("end") LocalDateTime end,
                                                                   @Param("uris") List<String> uris);
}

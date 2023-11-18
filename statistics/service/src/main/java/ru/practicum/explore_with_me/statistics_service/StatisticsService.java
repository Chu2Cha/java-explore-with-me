package ru.practicum.explore_with_me.statistics_service;

import ru.practicum.explore_with_me.statistics_dto.dto.EndpointHitDto;
import ru.practicum.explore_with_me.statistics_dto.dto.ViewStats;

import java.util.List;

public interface StatisticsService {
    EndpointHitDto postEndpointHit(EndpointHitDto endpointHitDto);

    List<ViewStats> getStats(String start, String end, List<String> uris, boolean unique);
}

package statservice.statistics_service;

import statdto.dto.EndpointHitDto;
import statdto.dto.ViewStats;

import java.util.List;

public interface StatisticsService {
    EndpointHitDto postEndpointHit(EndpointHitDto endpointHitDto);

    List<ViewStats> getStats(String start, String end, List<String> uris, boolean unique);
}

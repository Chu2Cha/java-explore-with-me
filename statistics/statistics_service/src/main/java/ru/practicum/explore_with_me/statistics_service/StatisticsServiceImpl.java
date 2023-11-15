package ru.practicum.explore_with_me.statistics_service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.statistics_dto.dto.EndpointHitDto;
import ru.practicum.explore_with_me.statistics_dto.dto.ViewStatsDto;
import ru.practicum.explore_with_me.statistics_mapper.EndpointHitMapper;
import ru.practicum.explore_with_me.statistics_mapper.ViewStatsMapper;
import ru.practicum.explore_with_me.statistics_model.EndpointHit;
import ru.practicum.explore_with_me.statistics_model.ViewStats;
import ru.practicum.explore_with_me.statistics_repository.StatisticsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.explore_with_me.statistics_dto.constants.Constants.formatter;

@Service
@AllArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsRepository repository;
    private final EndpointHitMapper endpointHitMapper;
    private final ViewStatsMapper viewStatsMapper;

    @Override
    public EndpointHitDto postEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = endpointHitMapper.toEndpointHit(endpointHitDto);
        return endpointHitMapper.toEndpointHitDto(repository.save(endpointHit));
    }

    @Override
    public List<ViewStatsDto> getStats(String start, String end, List<String> uris, boolean unique) {
        LocalDateTime startTime = stringToDate(start);
        LocalDateTime endTime = stringToDate(end);

        List<ViewStats> viewStatsList;
        if(unique){
            viewStatsList = repository.findAllUniqueWhenUriIsNotEmpty(startTime, endTime, uris);
        }
        else {
            viewStatsList = repository.findAllNotUniqueWhenUriIsNotEmpty(startTime, endTime, uris);
        }
        return viewStatsList.stream().map(viewStatsMapper::toViewStatsDto).collect(Collectors.toList());


    }

    private LocalDateTime stringToDate(String dateString) {
        return LocalDateTime.parse(dateString, formatter);
    }
}

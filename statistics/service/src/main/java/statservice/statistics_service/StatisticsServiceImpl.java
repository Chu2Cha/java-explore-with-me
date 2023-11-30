package statservice.statistics_service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import statdto.dto.EndpointHitDto;
import statservice.statistics_mapper.EndpointHitMapper;
import statservice.statistics_model.EndpointHit;
import statdto.dto.ViewStats;
import statservice.statistics_repository.StatisticsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static statdto.constants.Constants.formatter;

@Service
@AllArgsConstructor
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsRepository repository;
    private final EndpointHitMapper endpointHitMapper;

    @Override
    public EndpointHitDto postEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = endpointHitMapper.toEndpointHit(endpointHitDto);
        return endpointHitMapper.toEndpointHitDto(repository.save(endpointHit));
    }

    @Override
    public List<ViewStats> getStats(String start, String end, List<String> uris, boolean unique) {
        LocalDateTime startTime = stringToDate(start);
        LocalDateTime endTime = stringToDate(end);

        List<ViewStats> viewStatsList = new ArrayList<>();

        if (unique && (uris != null)) {
            log.info("getStats: ip is unique, uris are not empty");
            viewStatsList = repository.findAllUniqueWhenUriIsNotEmpty(startTime, endTime, uris);
        }
        if (!unique && (uris != null)) {
            log.info("getStats: ip is not unique, uris are not empty");
            viewStatsList = repository.findAllNotUniqueWhenUriIsNotEmpty(startTime, endTime, uris);
        }
        if (unique && (uris == null)) {
            log.info("getStats: ip is unique, uris are empty");
            viewStatsList = repository.findAllUniqueWhenUriIsEmpty(startTime, endTime);
        }
        if (!unique && (uris == null)) {
            log.info("getStats: ip is not unique, uris are empty");
            viewStatsList = repository.findAllNotUniqueWhenUriIsEmpty(startTime, endTime);
        }
        return viewStatsList;

    }

    private LocalDateTime stringToDate(String dateString) {
        return LocalDateTime.parse(dateString, formatter);
    }
}

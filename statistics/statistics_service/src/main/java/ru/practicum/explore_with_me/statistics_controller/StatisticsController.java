package ru.practicum.explore_with_me.statistics_controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.statistics_dto.dto.EndpointHitDto;
import ru.practicum.explore_with_me.statistics_model.ViewStats;
import ru.practicum.explore_with_me.statistics_service.StatisticsService;

import java.util.List;

@RestController
@Slf4j
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @PostMapping("/hit")
    public @ResponseStatus(HttpStatus.CREATED) EndpointHitDto postEndpointHit (@RequestBody EndpointHitDto endpointHitDto){
        log.info("Statistics-Service: Post request to /hit");
        return statisticsService.postEndpointHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public @ResponseStatus(HttpStatus.OK) List<ViewStats> getStats(@RequestParam String start,
                                                                   @RequestParam String end,
                                                                   @RequestParam (required = false) List<String> uris,
                                                                   @RequestParam (defaultValue = "false") boolean unique){
        log.info("Statistics-Service: Get request from /stats: start {}, end {}, uris {}, unique {}",
                start, end, uris, unique);
        return statisticsService.getStats(start, end, uris, unique);
    }

}

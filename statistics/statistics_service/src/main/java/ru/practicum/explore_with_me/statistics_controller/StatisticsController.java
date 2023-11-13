package ru.practicum.explore_with_me.statistics_controller;


import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.statistics_dto.EndpointHitDto;
import ru.practicum.explore_with_me.statistics_dto.ViewStatsDto;
import ru.practicum.explore_with_me.statistics_service.StatisticsService;

import java.util.List;

@RestController
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @PostMapping("/hit")
    public EndpointHitDto postEndpointHit (@RequestBody EndpointHitDto endpointHitDto){
        return statisticsService.postEndpointHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam String start,
                                 @RequestParam String end,
                                 @RequestParam (required = false) List<String> uris,
                                 @RequestParam (defaultValue = "false") boolean unique){
        return statisticsService.getStats(start, end, uris, unique);
    }

}

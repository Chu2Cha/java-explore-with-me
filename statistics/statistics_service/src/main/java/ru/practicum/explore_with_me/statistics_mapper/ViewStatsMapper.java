package ru.practicum.explore_with_me.statistics_mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.statistics_dto.dto.ViewStatsDto;
import ru.practicum.explore_with_me.statistics_model.ViewStats;

@Component
@AllArgsConstructor
public class ViewStatsMapper {
    public ViewStats toViewStats (ViewStatsDto viewStatsDto){
        return ViewStats.builder()
                .app(viewStatsDto.getApp())
                .uri(viewStatsDto.getUri())
                .hits(viewStatsDto.getHits())
                .build();
    }
    public ViewStatsDto toViewStatsDto(ViewStats viewStats){
        return ViewStatsDto.builder()
                .app(viewStats.getApp())
                .uri(viewStats.getUri())
                .hits(viewStats.getHits())
                .build();
    }
}

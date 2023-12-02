package ru.practicum.explore_with_me.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.compilation.CompilationDto;
import ru.practicum.explore_with_me.dto.compilation.NewCompilationDto;
import ru.practicum.explore_with_me.dto.event.EventShortDto;
import ru.practicum.explore_with_me.model.Compilation;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.service.EventValidation;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CompilationMapper {
    private final EventValidation eventValidation;
    private final EventMapper eventMapper;

    public Compilation fromNewToCompilation(NewCompilationDto newCompilationDto) {
        List<Event> eventList = newCompilationDto.getEvents() != null ?
                newCompilationDto.getEvents().stream()
                        .map(eventValidation::findEvent)
                        .collect(Collectors.toList()) :
                Collections.emptyList();
        Boolean pinned = newCompilationDto.getPinned() != null ? newCompilationDto.getPinned() : false;
        return Compilation.builder()
                .events(eventList)
                .pinned(pinned)
                .title(newCompilationDto.getTitle())
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        List<EventShortDto> eventShortDtoList = compilation.getEvents().stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
        return CompilationDto.builder()
                .events(eventShortDtoList)
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();

    }
}

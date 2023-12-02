package ru.practicum.explore_with_me.service.interfaces;

import ru.practicum.explore_with_me.dto.compilation.CompilationDto;

import java.util.List;

public interface PublicCompilationService {
    CompilationDto getCompilation(Long compId);

    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);
}

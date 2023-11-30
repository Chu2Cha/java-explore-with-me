package ru.practicum.explore_with_me.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.compilation.CompilationDto;
import ru.practicum.explore_with_me.exceptions.NotFoundException;
import ru.practicum.explore_with_me.mapper.CompilationMapper;
import ru.practicum.explore_with_me.model.Compilation;
import ru.practicum.explore_with_me.repository.CompilationRepository;
import ru.practicum.explore_with_me.service.interfaces.PublicCompilationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PublicCompilationServiceImpl implements PublicCompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;


    @Override
    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = findCompilation(compId);
        return compilationMapper.toCompilationDto(compilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        PageRequest page = PageRequest.of(from / size, size);
        List<Compilation> compilationList;
        if (pinned != null) {
            compilationList = compilationRepository.findAllByPinned(pinned, page);
        } else {
            compilationList = compilationRepository.findAll(page).toList();
        }
        return compilationList.stream()
                .map(compilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    private Compilation findCompilation(Long compId) {
        return compilationRepository.findById(compId).
                orElseThrow(() -> new NotFoundException("Подборка id =" + compId + " не найдена."));
    }
}

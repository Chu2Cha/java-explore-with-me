package ru.practicum.explore_with_me.controller.admin_c;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.compilation.CompilationDto;
import ru.practicum.explore_with_me.dto.compilation.NewCompilationDto;
import ru.practicum.explore_with_me.dto.compilation.UpdateCompilationRequest;
import ru.practicum.explore_with_me.service.interfaces.AdminCompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@Slf4j
@Validated
public class AdminCompilationController {
    private final AdminCompilationService compilationService;

    public AdminCompilationController(AdminCompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping
    public @ResponseStatus(HttpStatus.CREATED) CompilationDto postCompilation
            (@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Post new compilation {}", newCompilationDto);
        return compilationService.postCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public @ResponseStatus(HttpStatus.NO_CONTENT) void deleteCompilation(@PathVariable Long compId) {
        log.info("Delete compilation od = {}", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public @ResponseStatus(HttpStatus.OK) CompilationDto patchCompilation
            (@PathVariable Long compId,
             @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.info("update compilation id = {}, update request = {}", compId, updateCompilationRequest);
        return compilationService.patchCompilation(compId, updateCompilationRequest);
    }
}

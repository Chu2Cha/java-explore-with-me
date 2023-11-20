package ru.practicum.explore_with_me.controller.admin_c;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.category.CategoryDto;
import ru.practicum.explore_with_me.dto.category.NewCategoryDto;
import ru.practicum.explore_with_me.service.admin_s.category.AdminCategoryService;

@RestController
@RequestMapping(path = "/admin/categories")
@Slf4j
public class AdminCategoryController {
    private final AdminCategoryService categoryService;

    public AdminCategoryController(AdminCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public @ResponseStatus(HttpStatus.CREATED) CategoryDto postUser(@RequestBody NewCategoryDto newCategoryDto) {
        log.info("post newCategoryDto:" + newCategoryDto);
        return categoryService.postCategory(newCategoryDto);
    }

    @DeleteMapping("/{id}")
    public @ResponseStatus(HttpStatus.NO_CONTENT) void delete(@PathVariable("id") long id) {
        log.info("delete category id = " + id);
        categoryService.deleteCategory(id);
    }

    @PatchMapping("/{id}")
    public @ResponseStatus(HttpStatus.OK) CategoryDto patchCategory(@PathVariable("id") long id,
                                     @RequestBody CategoryDto categoryDto) {
        log.info("patch categoryDto " + categoryDto);
        return categoryService.patchCategory(id, categoryDto);
    }

}

package ru.practicum.explore_with_me.service.interfaces;

import ru.practicum.explore_with_me.dto.category.CategoryDto;

import java.util.List;

public interface PublicCategoryService {
    List<CategoryDto> getAllCategories(int from, int size);

    CategoryDto getCategory(long id);
}

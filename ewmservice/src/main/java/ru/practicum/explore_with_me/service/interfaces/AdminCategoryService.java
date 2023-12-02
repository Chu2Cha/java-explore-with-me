package ru.practicum.explore_with_me.service.interfaces;

import ru.practicum.explore_with_me.dto.category.CategoryDto;
import ru.practicum.explore_with_me.dto.category.NewCategoryDto;

public interface AdminCategoryService {
    CategoryDto postCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(long id);


    CategoryDto patchCategory(long id, CategoryDto categoryDto);
}

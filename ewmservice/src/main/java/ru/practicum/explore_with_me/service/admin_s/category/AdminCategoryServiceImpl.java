package ru.practicum.explore_with_me.service.admin_s.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.category.CategoryDto;
import ru.practicum.explore_with_me.dto.category.NewCategoryDto;
import ru.practicum.explore_with_me.exceptions.ConflictException;
import ru.practicum.explore_with_me.exceptions.NotFoundException;
import ru.practicum.explore_with_me.mapper.CategoryMapper;
import ru.practicum.explore_with_me.model.Category;
import ru.practicum.explore_with_me.repository.CategoryRepository;

import java.util.List;


@Service
@Slf4j
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper = new CategoryMapper();

    public AdminCategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto postCategory(NewCategoryDto newCategoryDto) {
        checkCategoryForPost(newCategoryDto.getName());
        Category category = categoryMapper.toCategoryFromNew(newCategoryDto);
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(long id) {
        CategoryDto categoryForDelete = findCategoryById(id);
        log.info("Delete category: " + categoryForDelete);
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto patchCategory(long id, CategoryDto categoryDto) {
        Category category = categoryMapper.toCategory(findCategoryById(id));
        checkCategoryForUpdate(categoryDto.getName(), id);
        category.setName(categoryDto.getName());
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    private CategoryDto findCategoryById(long id) {
        return categoryMapper.toCategoryDto(categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория с id " + id + " не найдена.")));
    }

    private void checkCategoryForPost(String name) {
        if (!categoryRepository.findOneByName(name).isEmpty()) {
            throw new ConflictException("Ошибка: категория " + name + " уже существует");
        }
    }

    // не понял практического смысла, но раз тесты требуют: Postman - Изменение категории с неизменными данными -
    // Ответ должен содержать код статуса 200 и данные в формате json
    private void checkCategoryForUpdate(String name, long id) {
        List<Category> categoryList = categoryRepository.findOneByName(name);
        if (!categoryList.isEmpty() && categoryList.get(0).getId() != id) {
            throw new ConflictException("Ошибка: категория " + name + " уже существует");
        }
    }
}

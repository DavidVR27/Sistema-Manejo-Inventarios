package service;

import dto.CategoryDTO;
import dto.Response;

public interface CategoryService {
    Response createCategory(CategoryDTO categoryDTO);

    Response getAllCategories();

    Response getCategoryById(Long id);

    Response updateCategory(Long id, CategoryDTO categoryDTO);

    Response createCategory(Long id);
}

package com.ecommerce.backend.service.category;

import com.ecommerce.backend.dto.category.CategoryRequest;
import com.ecommerce.backend.dto.category.CategoryResponse;


public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryResponse createCategory(CategoryRequest categoryRequest);
    void deleteCategory(Long categoryId);
    CategoryResponse updateCategory(CategoryRequest categoryRequest,Long categoryId);
}

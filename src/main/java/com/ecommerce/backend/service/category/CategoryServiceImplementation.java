package com.ecommerce.backend.service.category;

import com.ecommerce.backend.Exceptions.APIException;
import com.ecommerce.backend.Exceptions.ResourceNotFoundException;
import com.ecommerce.backend.configs.AppConstants;
import com.ecommerce.backend.dto.category.CategoryRequest;
import com.ecommerce.backend.dto.category.CategoryResponse;
import com.ecommerce.backend.entity.category.Category;
import com.ecommerce.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImplementation implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;


    @Override
    public CategoryResponse getAllCategories(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortOrder
    ) {
        // Use defaults if null
        int page = (pageNumber == null) ? Integer.parseInt(AppConstants.PAGE_NUMBER) : pageNumber;
        int size = (pageSize == null) ? Integer.parseInt(AppConstants.PAGE_SIZE) : pageSize;
        String sortField = (sortBy == null) ? AppConstants.SORT_CATEGORIES_BY : sortBy;
        String sortDir = (sortOrder == null) ? AppConstants.SORT_DIR : sortOrder;

        // Sorting
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        // Fetch paginated data
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        if (categoryPage.isEmpty()) {
            throw new APIException("No categories found.");
        }


        List<CategoryResponse.CategoryData> categoryList = categoryPage.getContent()
                .stream()
                .map(category -> modelMapper.map(category, CategoryResponse.CategoryData.class))
                .toList();


        return CategoryResponse.builder()
                .content(categoryList)
                .pageNumber(categoryPage.getNumber())
                .pageSize(categoryPage.getSize())
                .totalElements(categoryPage.getTotalElements())
                .totalPages(categoryPage.getTotalPages())
                .lastPage(categoryPage.isLast())
                .build();
    }


    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Category existingCategory = categoryRepository.findByCategoryName(categoryRequest.getCategoryName());
        if (existingCategory != null) {
            throw new APIException(
                    "Category with name '" + categoryRequest.getCategoryName() + "' already exists"
            );
        }

        Category category = modelMapper.map(categoryRequest, Category.class);
        Category savedCategory = categoryRepository.save(category);

        CategoryResponse.CategoryData categoryData = modelMapper.map(savedCategory, CategoryResponse.CategoryData.class);

        return CategoryResponse.builder()
                .content(List.of(categoryData))
                .pageNumber(0)
                .pageSize(1)
                .totalElements(1L)
                .totalPages(1)
                .lastPage(true)
                .build();
    }


    @Override
    public CategoryResponse updateCategory(CategoryRequest categoryRequest, Long categoryId) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        existingCategory.setCategoryName(categoryRequest.getCategoryName());
        Category updatedCategory = categoryRepository.save(existingCategory);

        CategoryResponse.CategoryData categoryData = modelMapper.map(updatedCategory, CategoryResponse.CategoryData.class);

        return CategoryResponse.builder()
                .content(List.of(categoryData))
                .pageNumber(0)
                .pageSize(1)
                .totalElements(1L)
                .totalPages(1)
                .lastPage(true)
                .build();
    }


    @Override
    public CategoryResponse deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        categoryRepository.delete(category);

        CategoryResponse.CategoryData categoryData = modelMapper.map(category, CategoryResponse.CategoryData.class);

        return CategoryResponse.builder()
                .content(List.of(categoryData))
                .pageNumber(0)
                .pageSize(1)
                .totalElements(1L)
                .totalPages(1)
                .lastPage(true)
                .build();
    }
}

package com.ecommerce.backend.controller;

import com.ecommerce.backend.configs.AppConstants;
import com.ecommerce.backend.dto.category.CategoryRequest;
import com.ecommerce.backend.dto.category.CategoryResponse;
import com.ecommerce.backend.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ){
        return ResponseEntity.ok(categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder));
    }
    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest categoryRequest){
        return ResponseEntity.ok(categoryService.createCategory(categoryRequest));
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryResponse> deleteCategory(@PathVariable Long categoryId){
        return ResponseEntity.ok(categoryService.deleteCategory(categoryId));
    }
    @PostMapping
    public ResponseEntity<CategoryResponse> updateCategory(@Valid @RequestBody CategoryRequest categoryRequest,Long categoryId){
        return ResponseEntity.ok(categoryService.updateCategory(categoryRequest,categoryId));
    }

}

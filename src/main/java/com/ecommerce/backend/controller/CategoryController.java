package com.ecommerce.backend.controller;

import com.ecommerce.backend.entity.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {
    private List<Category> categories=new ArrayList<>();


    @GetMapping("/api/public/categories")
    private List<Category> getAllCategories(){
        return  categories;
    }

    @PostMapping("/api/admin/categories")
    private List<Category> createCategories(@RequestBody Category category){
        categories.add(category);
        return categories;
    }
}

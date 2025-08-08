package com.staj.gib.shopapi.controller;

import com.staj.gib.shopapi.dto.request.CreateCategoryRequest;
import com.staj.gib.shopapi.dto.request.UpdateCategoryRequest;
import com.staj.gib.shopapi.dto.response.CategoryResponse;
import com.staj.gib.shopapi.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @GetMapping
    public List<CategoryResponse> all() {
        return service.getAllCategories();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse newCategory(@Valid @RequestBody CreateCategoryRequest newCategory) {
        return service.createCategory(newCategory);
    }

    @GetMapping("/{id}")
    public CategoryResponse one(@PathVariable @NotNull UUID id) {
        return service.getCategory(id);
    }

    @PutMapping
    public CategoryResponse replaceCategory(@Valid @RequestBody UpdateCategoryRequest newCategory) {
        return service.updateCategory(newCategory);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable @NotNull UUID id) {
        service.deleteCategory(id);
    }
}
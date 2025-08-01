package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.dto.mapper.CategoryMapper;
import com.staj.gib.shopapi.dto.request.CategoryTaxRequest;
import com.staj.gib.shopapi.dto.request.CreateCategoryRequest;
import com.staj.gib.shopapi.dto.request.UpdateCategoryRequest;
import com.staj.gib.shopapi.dto.response.CategoryResponse;
import com.staj.gib.shopapi.entity.*;
import com.staj.gib.shopapi.exception.ResourceNotFoundException;
import com.staj.gib.shopapi.repository.ProductCategoryRepository;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final ProductCategoryRepository repository;
    private final CategoryMapper mapper;
    private final EntityManager entityManager;

    public CategoryResponse getCategory(UUID categoryId) {
        ProductCategory category = repository.findById(categoryId).orElseThrow(()
                -> new ResourceNotFoundException("ProductCategory",categoryId));

        return mapper.mapCategory(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return repository.findAll()
                .stream()
                .map(mapper::mapCategory)
                .toList();
    }

    public CategoryResponse createCategory(CreateCategoryRequest request) {
        ProductCategory category = new ProductCategory(request.getCategoryName(), new ArrayList<>());
        for(CategoryTaxRequest c : request.getTaxes()){
            Tax taxReference = entityManager.getReference(Tax.class, c.getTaxId());
            category.getTaxes().add(new ProductCategoryTax(category, taxReference, c.getTaxPercent()));
        }

        ProductCategory saved = repository.save(category);
        return mapper.mapCategory(saved);
    }

    public CategoryResponse updateCategory(UpdateCategoryRequest request) {
        ProductCategory category = repository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("ProductCategory", request.getId()));

        category.setCategoryName(request.getCategoryName());

        if (request.getTaxes() != null) {
            // orphan removal
            category.getTaxes().clear();

            for(CategoryTaxRequest taxRequest : request.getTaxes()) {
                Tax taxReference = entityManager.getReference(Tax.class, taxRequest.getTaxId());
                ProductCategoryTax categoryTax = new ProductCategoryTax(category, taxReference, taxRequest.getTaxPercent());
                category.getTaxes().add(categoryTax);
            }
        }

        ProductCategory saved = repository.save(category);
        return mapper.mapCategory(saved);
    }

    public void deleteCategory(UUID categoryId) {
        if (!repository.existsById(categoryId)) {
            throw new ResourceNotFoundException("ProductCategory", categoryId);
        }

        repository.deleteById(categoryId);
    }
}

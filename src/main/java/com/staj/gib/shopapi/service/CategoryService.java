package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.dto.mapper.CategoryMapper;
import com.staj.gib.shopapi.dto.mapper.TaxMapper;
import com.staj.gib.shopapi.dto.request.CategoryTaxRequest;
import com.staj.gib.shopapi.dto.request.CreateCategoryRequest;
import com.staj.gib.shopapi.dto.request.UpdateCategoryRequest;
import com.staj.gib.shopapi.dto.response.CategoryResponse;
import com.staj.gib.shopapi.entity.ProductCategory;
import com.staj.gib.shopapi.entity.ProductCategoryTax;
import com.staj.gib.shopapi.enums.ErrorCode;
import com.staj.gib.shopapi.exception.BusinessException;
import com.staj.gib.shopapi.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final ProductCategoryRepository repository;
    private final CategoryMapper mapper;
    private final TaxMapper taxMapper;

    public CategoryResponse getCategory(UUID categoryId) {
        ProductCategory category = repository.findById(categoryId).orElseThrow(()
                -> new BusinessException(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND, categoryId));

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
        ProductCategory category = mapper.requestToCategory(request);

        category.getTaxes().forEach(tax -> tax.setCategory(category));

        ProductCategory saved = repository.save(category);
        return mapper.mapCategory(saved);
    }

    public CategoryResponse updateCategory(UpdateCategoryRequest request) {
        ProductCategory category = repository.findById(request.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND, request.getId()));

        category.setCategoryName(request.getCategoryName());

        if (request.getTaxes() != null) {
            // orphan removal
            category.getTaxes().clear();

            for(CategoryTaxRequest taxRequest : request.getTaxes()) {
                ProductCategoryTax categoryTax = mapper.requestToCategoryTax(taxRequest);
                categoryTax.setCategory(category);
                category.getTaxes().add(categoryTax);
            }
        }

        ProductCategory saved = repository.save(category);
        return mapper.mapCategory(saved);
    }

    public void deleteCategory(UUID categoryId) {
        if (!repository.existsById(categoryId)) {
            throw new BusinessException(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND, categoryId);
        }

        repository.deleteById(categoryId);
    }
}

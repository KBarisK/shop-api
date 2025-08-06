package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.dto.mapper.ProductMapper;
import com.staj.gib.shopapi.dto.request.CreateProductRequest;
import com.staj.gib.shopapi.dto.request.TaxRequest;
import com.staj.gib.shopapi.dto.request.UpdateProductRequest;
import com.staj.gib.shopapi.dto.response.ProductResponse;
import com.staj.gib.shopapi.dto.response.TaxResponse;
import com.staj.gib.shopapi.entity.Product;
import com.staj.gib.shopapi.entity.Tax;
import com.staj.gib.shopapi.exception.ResourceNotFoundException;
import com.staj.gib.shopapi.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Transactional(readOnly = true)
    public ProductResponse getProduct(UUID productid) {
        Product product = repository.findById(productid).orElseThrow(()
                -> new ResourceNotFoundException("Product",productid));

        return mapper.toResponse(product);
    }

    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = mapper.toEntity(request);

        return mapper.toResponse(this.repository.save(product));
    }

    public ProductResponse updateProduct(UpdateProductRequest request){
        Product existingProduct = repository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", request.getId()));

        // update only non-null fields
        mapper.updateEntity(existingProduct, request);
        existingProduct.getImages().forEach(img -> img.setProduct(existingProduct));

        return mapper.toResponse(repository.save(existingProduct));
    }

    public void deleteProductById(UUID id){
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Product", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public void decrementStock(UUID productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");

        Product product = repository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Not enough stock");
        }

        product.setStock(product.getStock() - quantity);
        repository.save(product); // version prevents lost update
    }
}

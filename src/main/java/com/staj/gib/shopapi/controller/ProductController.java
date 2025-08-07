package com.staj.gib.shopapi.controller;

import com.staj.gib.shopapi.dto.request.CreateProductRequest;
import com.staj.gib.shopapi.dto.request.UpdateProductRequest;
import com.staj.gib.shopapi.dto.response.ProductResponse;
import com.staj.gib.shopapi.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService service;

    @GetMapping
    public List<ProductResponse> all() {
        return service.getAllProducts();
    }

    // todo when we have image hosting, do not accept URL, instead get the image and upload
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse newProduct(@Valid @RequestBody CreateProductRequest newProduct) {
        return service.createProduct(newProduct);
    }

    @GetMapping("/{id}")
    public ProductResponse one(@PathVariable UUID id) {
        return service.getProduct(id);
    }

    @PutMapping
    public ProductResponse replaceProduct(@Valid @RequestBody UpdateProductRequest updateProduct) {
        return service.updateProduct(updateProduct);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable UUID id) {
        service.deleteProductById(id);
    }
}
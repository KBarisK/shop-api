package com.staj.gib.shopapi.controller;

import com.staj.gib.shopapi.dto.request.CreateProductRequest;
import com.staj.gib.shopapi.dto.request.UpdateProductRequest;
import com.staj.gib.shopapi.dto.response.ProductResponse;
import com.staj.gib.shopapi.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(
            @RequestPart("product") @Valid CreateProductRequest product,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        return service.createProduct(product, images); // change service signature
    }

    @GetMapping("/{id}")
    public ProductResponse one(@PathVariable @NotNull UUID id) {
        return service.getProduct(id);
    }


    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductResponse updateProduct(
            @RequestPart("product") @Valid UpdateProductRequest product,
            @RequestPart(value = "newImages", required = false) List<MultipartFile> newImages,
            @RequestParam(value = "removeImageIds", required = false) List<UUID> removeImageIds
    ) {
        return service.updateProduct(product, newImages, removeImageIds); // change service signature
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable @NotNull UUID id) {
        service.deleteProductById(id);
    }

    @GetMapping("/search/{keyword}")
    public List<ProductResponse> search(@PathVariable String keyword) {
        return service.getProductsByKeyword(keyword);
    }
}

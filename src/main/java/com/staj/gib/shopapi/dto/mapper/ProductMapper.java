package com.staj.gib.shopapi.dto.mapper;

import com.staj.gib.shopapi.dto.request.CreateProductRequest;
import com.staj.gib.shopapi.dto.request.UpdateProductRequest;
import com.staj.gib.shopapi.dto.response.ProductImageResponse;
import com.staj.gib.shopapi.dto.response.ProductResponse;
import com.staj.gib.shopapi.entity.Product;
import com.staj.gib.shopapi.entity.ProductImage;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.UUID;

@Mapper(config = CentralMapperConfig.class, uses = ProductImageMapper.class)
public interface ProductMapper {
    @Mapping(target = "categoryId", source = "entity.category.id")
    @Mapping(target = "images", source = "entity.images")
    ProductResponse toResponse(Product entity, BigDecimal afterTaxPrice);

    Product toEntity(CreateProductRequest request);

    void updateEntity(@MappingTarget Product existingProduct, UpdateProductRequest request);
}
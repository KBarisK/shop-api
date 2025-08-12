package com.staj.gib.shopapi.dto.mapper;

import com.staj.gib.shopapi.dto.request.CreateProductRequest;
import com.staj.gib.shopapi.dto.request.UpdateProductRequest;
import com.staj.gib.shopapi.dto.response.ProductResponse;
import com.staj.gib.shopapi.entity.Product;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.UUID;

@Mapper(config = CentralMapperConfig.class, uses = ProductImageMapper.class)
public interface ProductMapper {

    @Mapping(target = "categoryId", source = "entity.category.id")
    ProductResponse toResponse(Product entity, BigDecimal afterTaxPrice);

    @Mapping(target = "images", source = "imageUrls")
    Product toEntity(CreateProductRequest request);

    @Mapping(target = "images", source = "imageUrls")
    void updateEntity(@MappingTarget Product existingProduct, UpdateProductRequest request);

}
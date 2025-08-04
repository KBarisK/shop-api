package com.staj.gib.shopapi.dto.mapper;

import com.staj.gib.shopapi.dto.response.ProductResponse;
import com.staj.gib.shopapi.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = ProductImageMapper.class)
public interface ProductMapper {

    @Mapping(target = "categoryId", source = "category.id")
    ProductResponse toResponse(Product entity);

    @Mapping(target = "id", source = "productId")
    Product productFromId(UUID productId);

}
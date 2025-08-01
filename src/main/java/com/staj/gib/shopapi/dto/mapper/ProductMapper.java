package com.staj.gib.shopapi.dto.mapper;

import com.staj.gib.shopapi.dto.response.ProductResponse;
import com.staj.gib.shopapi.entity.Product;
import com.staj.gib.shopapi.entity.ProductImage;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = ProductImageMapper.class)
public interface ProductMapper {

    @Mapping(target = "categoryId", source = "category.id")
    ProductResponse toResponse(Product entity);


}
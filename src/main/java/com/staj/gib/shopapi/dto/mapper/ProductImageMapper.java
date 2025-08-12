package com.staj.gib.shopapi.dto.mapper;

import com.staj.gib.shopapi.dto.response.ProductImageResponse;
import com.staj.gib.shopapi.entity.ProductImage;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface ProductImageMapper {
    ProductImageResponse toResponse(ProductImage image);
}


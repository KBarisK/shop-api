package com.staj.gib.shopapi.dto.mapper;
import com.staj.gib.shopapi.dto.response.CategoryResponse;
import com.staj.gib.shopapi.dto.response.CategoryTaxResponse;
import com.staj.gib.shopapi.entity.ProductCategory;
import com.staj.gib.shopapi.entity.ProductCategoryTax;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse mapCategory(ProductCategory entity);

    @Mapping(target = "taxId", source = "tax.id")
    @Mapping(target = "taxName", source = "tax.taxName")
    CategoryTaxResponse mapCategoryTax(ProductCategoryTax categoryTax);


}
package com.staj.gib.shopapi.dto.mapper;

import com.staj.gib.shopapi.entity.Tax;
import com.staj.gib.shopapi.dto.request.TaxRequest;
import com.staj.gib.shopapi.dto.request.UpdateTaxRequest;
import com.staj.gib.shopapi.dto.response.TaxResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TaxMapper {

    Tax toEntity(TaxRequest request);

    TaxResponse toResponse(Tax tax);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateTaxRequest dto, @MappingTarget Tax tax);
}

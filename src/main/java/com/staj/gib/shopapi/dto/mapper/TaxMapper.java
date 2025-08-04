package com.staj.gib.shopapi.dto.mapper;

import com.staj.gib.shopapi.dto.request.TaxRequest;
import com.staj.gib.shopapi.dto.request.UpdateTaxRequest;
import com.staj.gib.shopapi.dto.response.TaxResponse;
import com.staj.gib.shopapi.entity.Tax;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TaxMapper {

    Tax toEntity(TaxRequest request);

    TaxResponse toResponse(Tax tax);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateTaxRequest dto, @MappingTarget Tax tax);

    Tax getFromId(UUID taxId);
}

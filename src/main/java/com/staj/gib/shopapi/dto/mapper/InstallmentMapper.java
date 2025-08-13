package com.staj.gib.shopapi.dto.mapper;

import com.staj.gib.shopapi.dto.response.InstallmentDto;
import com.staj.gib.shopapi.entity.Installment;
import org.mapstruct.*;

@Mapper(config = CentralMapperConfig.class)
public interface InstallmentMapper {
    Installment toEntity(InstallmentDto installmentDto);

    InstallmentDto toDto(Installment installment);

    Installment partialUpdate(InstallmentDto installmentDto, @MappingTarget Installment installment);
}
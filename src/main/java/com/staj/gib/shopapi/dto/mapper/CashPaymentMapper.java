package com.staj.gib.shopapi.dto.mapper;

import com.staj.gib.shopapi.entity.CashPayment;
import com.staj.gib.shopapi.dto.response.CashPaymentDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CashPaymentMapper {
    CashPayment toEntity(CashPaymentDto cashPaymentDto);

    CashPaymentDto toDto(CashPayment cashPayment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CashPayment partialUpdate(CashPaymentDto cashPaymentDto, @MappingTarget CashPayment cashPayment);
}
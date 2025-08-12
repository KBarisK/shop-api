package com.staj.gib.shopapi.dto.mapper;

import com.staj.gib.shopapi.dto.response.InstallmentPaymentDto;
import com.staj.gib.shopapi.entity.InstallmentPayment;
import org.mapstruct.*;

@Mapper(config = CentralMapperConfig.class)
public interface InstallmentPaymentMapper {
    InstallmentPayment toEntity(InstallmentPaymentDto installmentPaymentDto);

    @AfterMapping
    default void linkInstallments(@MappingTarget InstallmentPayment installmentPayment) {
        installmentPayment.getInstallments().forEach(installment -> installment.setInstallmentPayment(installmentPayment));
    }

    InstallmentPaymentDto toDto(InstallmentPayment installmentPayment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    InstallmentPayment partialUpdate(InstallmentPaymentDto installmentPaymentDto, @MappingTarget InstallmentPayment installmentPayment);
}
package com.staj.gib.shopapi.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.staj.gib.shopapi.constant.RoundingConstants;
import com.staj.gib.shopapi.dto.mapper.TaxMapper;
import com.staj.gib.shopapi.dto.request.TaxRequest;
import com.staj.gib.shopapi.dto.request.UpdateTaxRequest;
import com.staj.gib.shopapi.dto.response.CategoryTaxResponse;
import com.staj.gib.shopapi.dto.response.TaxDetailDto;
import com.staj.gib.shopapi.dto.response.TaxResponse;
import com.staj.gib.shopapi.entity.Tax;
import com.staj.gib.shopapi.enums.ErrorCode;
import com.staj.gib.shopapi.exception.BusinessException;
import com.staj.gib.shopapi.repository.TaxRepository;

import lombok.AllArgsConstructor;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class TaxService {
    private final TaxRepository repository;
    private final TaxMapper mapper;

    public List<TaxResponse> getAllTaxes() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')" )
    public TaxResponse createTax(TaxRequest request) {
        Tax tax = mapper.toEntity(request);
        tax = repository.save(tax);
        return mapper.toResponse(tax);
    }

    public TaxResponse getTaxById(UUID id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new BusinessException(ErrorCode.TAX_NOT_FOUND, id));

    }

    public TaxResponse getTaxByName(String name) {
        return repository.findByTaxName(name).map(mapper::toResponse)
                .orElseThrow(() -> new BusinessException(ErrorCode.TAX_NOT_FOUND, name));
    }

    @Transactional
    public void deleteTaxById(UUID id){
        if (!repository.existsById(id)) {
            throw new BusinessException(ErrorCode.TAX_NOT_FOUND, id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public TaxResponse replaceTax(UpdateTaxRequest request) {
        Tax tax = repository.findById(request.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.TAX_NOT_FOUND, request.getId()));
        mapper.updateFromDto(request, tax);
        tax = repository.save(tax);
        return mapper.toResponse(tax);
    }

    public TaxDetailDto calculateTax(BigDecimal amount, CategoryTaxResponse categoryTaxResponse) {
        BigDecimal taxPercent = categoryTaxResponse.getTaxPercent();
        BigDecimal taxAmount = amount.multiply(taxPercent).divide(BigDecimal.valueOf(100), RoundingConstants.SCALE, RoundingConstants.ROUNDING);
        return new TaxDetailDto(categoryTaxResponse.getTaxName(), taxAmount);
    }

    public List<TaxDetailDto> calculateTaxBreakdown(BigDecimal basePrice, List<CategoryTaxResponse> taxes) {
        List<TaxDetailDto> result = new ArrayList<>();
        for (CategoryTaxResponse tax : taxes) {
            TaxDetailDto detail = calculateTax(basePrice, tax);
            result.add(detail);
        }
        return result;
    }

}
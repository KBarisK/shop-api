package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.dto.mapper.TaxMapper;
import com.staj.gib.shopapi.dto.request.TaxRequest;
import com.staj.gib.shopapi.dto.request.UpdateTaxRequest;
import com.staj.gib.shopapi.dto.response.CategoryTaxResponse;
import com.staj.gib.shopapi.dto.response.TaxResponse;
import com.staj.gib.shopapi.entity.Tax;
import com.staj.gib.shopapi.exception.ResourceNotFoundException;
import com.staj.gib.shopapi.repository.TaxRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class TaxService {
    private final TaxRepository repository;
    private final TaxMapper mapper;

    @Transactional(readOnly = true)
    public List<TaxResponse> getAllTaxes() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public TaxResponse createTax(TaxRequest request) {
        Tax tax = mapper.toEntity(request);
        tax = repository.save(tax);
        return mapper.toResponse(tax);
    }

    @Transactional(readOnly = true)
    public TaxResponse getTaxById(UUID id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Tax", id));

    }

    public void deleteTaxById(UUID id){
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Tax", id);
        }
        repository.deleteById(id);
    }

    public TaxResponse replaceTax(UpdateTaxRequest request) {
        Tax tax = repository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Tax", request.getId()));
        mapper.updateFromDto(request, tax);
        tax = repository.save(tax);
        return mapper.toResponse(tax);
    }

    public BigDecimal calculateTax(BigDecimal amount, CategoryTaxResponse categoryTaxResponse) {
        BigDecimal taxPercent = categoryTaxResponse.getTaxPercent();
        return amount.multiply(taxPercent).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
    }

}
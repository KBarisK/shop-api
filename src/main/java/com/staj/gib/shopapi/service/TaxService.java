package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.exception.TaxNotFoundException;
import com.staj.gib.shopapi.entity.Tax;
import com.staj.gib.shopapi.entity.dto.TaxRequest;
import com.staj.gib.shopapi.entity.dto.TaxResponse;
import com.staj.gib.shopapi.repository.TaxRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class TaxService {
    private final TaxRepository repository;

    @Transactional(readOnly = true)
    public List<TaxResponse> getAllTaxes() {
        return repository.findAll()
                .stream()
                .map(TaxResponse::fromEntity)
                .toList();
    }

    public TaxResponse createTax(TaxRequest request) {
        Tax tax = repository.save(request.toEntity());
        return TaxResponse.fromEntity(tax);
    }

    @Transactional(readOnly = true)
    public TaxResponse getTaxById(UUID id) {
        return repository.findById(id).map(TaxResponse::fromEntity)
                .orElseThrow(() -> new TaxNotFoundException(id));

    }

    public void deleteTaxById(UUID id){
        if (!repository.existsById(id)) {
            throw new TaxNotFoundException(id);
        }
        repository.deleteById(id);
    }

    public TaxResponse replaceTax(TaxRequest request, UUID id) {
        return repository.findById(id)
                .map(tax -> {
                    tax.setTaxName(request.getTaxName());
                    return TaxResponse.fromEntity(repository.save(tax));
                })
                .orElseThrow(() -> new TaxNotFoundException(id));
    }

}
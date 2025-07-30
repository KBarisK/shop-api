package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.exception.NotFoundException;
import com.staj.gib.shopapi.exception.TaxAlreadyExistsException;
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
        if (repository.existsByTaxNameIgnoreCase(request.getTaxName())) {
            throw new TaxAlreadyExistsException("Tax with name '" + request.getTaxName() + "' already exists");
        }

        Tax tax = repository.save(request.toEntity());
        return TaxResponse.fromEntity(tax);
    }

    @Transactional(readOnly = true)
    public TaxResponse getTaxById(UUID id) {
        return repository.findById(id).map(TaxResponse::fromEntity)
                .orElseThrow(() -> new NotFoundException(id));

    }

    public void deleteTaxById(UUID id){
        if (!repository.existsById(id)) {
            throw new NotFoundException(id);
        }
        repository.deleteById(id);
    }

    public TaxResponse replaceTax(TaxRequest request, UUID id) {
        return repository.findById(id)
                .map(tax -> {
                    // check uniqueness for updates and exclude current tax
                    if (!tax.getTaxName().equalsIgnoreCase(request.getTaxName()) &&
                            repository.existsByTaxNameIgnoreCase(request.getTaxName())) {
                        throw new TaxAlreadyExistsException("Tax with name '" + request.getTaxName() + "' already exists");
                    }

                    tax.setTaxName(request.getTaxName());
                    return TaxResponse.fromEntity(repository.save(tax));
                })
                .orElseThrow(() -> new NotFoundException(id));
    }

}
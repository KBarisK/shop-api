package com.staj.gib.shopapi.repository;

import com.staj.gib.shopapi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("""
        SELECT p FROM Product p
        WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(CAST(p.description AS string)) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<Product> search(@Param("keyword") String keyword);
}
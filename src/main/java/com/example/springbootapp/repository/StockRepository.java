package com.example.springbootapp.repository;

import com.example.springbootapp.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Page<Stock> findBySymbolContainingIgnoreCaseOrCompanyNameContainingIgnoreCase(
            String symbol,
            String companyName,
            Pageable pageable
    );

    @Query(value = "SELECT * FROM stocks ORDER BY id ASC LIMIT :limit", nativeQuery = true)
    List<Stock> findTopNByOrderByIdAsc(@Param("limit") int limit);

    @Query(value = "SELECT * FROM stocks ORDER BY price DESC LIMIT :limit", nativeQuery = true)
    List<Stock> findTopNByOrderByPriceDesc(@Param("limit") int limit);
}

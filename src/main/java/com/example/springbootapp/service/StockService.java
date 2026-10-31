package com.example.springbootapp.service;

import com.example.springbootapp.dto.StockRequest;
import com.example.springbootapp.dto.StockStatsResponse;
import com.example.springbootapp.entity.Stock;
import com.example.springbootapp.repository.StockRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Page<Stock> getStocks(String search, int page, int size, String sortBy, String direction) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(size, 1);

        String normalizedSortBy = (sortBy == null || sortBy.isBlank()) ? "id" : sortBy;
        String normalizedDirection = (direction == null || direction.isBlank()) ? "asc" : direction;

        Sort.Direction resolvedDirection;
        try {
            resolvedDirection = Sort.Direction.fromString(normalizedDirection);
        } catch (IllegalArgumentException ex) {
            resolvedDirection = Sort.Direction.ASC;
        }

        Sort sort = Sort.by(resolvedDirection, normalizedSortBy);
        Pageable pageable = PageRequest.of(safePage, safeSize, sort);

        if (search == null || search.isBlank()) {
            return stockRepository.findAll(pageable);
        }

        return stockRepository.findBySymbolContainingIgnoreCaseOrCompanyNameContainingIgnoreCase(
                search.trim(),
                search.trim(),
                pageable
        );
    }

    public Optional<Stock> getStockById(Long id) {
        return stockRepository.findById(id);
    }

    public Stock createStock(StockRequest request) {
        Stock stock = new Stock();
        stock.setSymbol(normalizeSymbol(request.getSymbol()));
        stock.setCompanyName(normalizeCompanyName(request.getCompanyName()));
        stock.setPrice(request.getPrice());
        stock.setQuantity(request.getQuantity());
        return stockRepository.save(stock);
    }

    public Optional<Stock> updateStock(Long id, StockRequest request) {
        return stockRepository.findById(id)
                .map(existing -> {
                    existing.setSymbol(normalizeSymbol(request.getSymbol()));
                    existing.setCompanyName(normalizeCompanyName(request.getCompanyName()));
                    existing.setPrice(request.getPrice());
                    existing.setQuantity(request.getQuantity());
                    return stockRepository.save(existing);
                });
    }

    public boolean deleteStock(Long id) {
        if (!stockRepository.existsById(id)) {
            return false;
        }
        stockRepository.deleteById(id);
        return true;
    }

    public StockStatsResponse getStockStats() {
        List<Stock> stocks = stockRepository.findAll();
        if (stocks.isEmpty()) {
            return new StockStatsResponse(0, 0, 0.0, 0.0, 0.0, 0.0, "N/A");
        }

        long totalQuantity = 0L;
        double totalValue = 0.0;
        double highestPrice = Double.NEGATIVE_INFINITY;
        double lowestPrice = Double.POSITIVE_INFINITY;

        for (Stock stock : stocks) {
            totalQuantity += stock.getQuantity();
            totalValue += stock.getPrice() * stock.getQuantity();
            highestPrice = Math.max(highestPrice, stock.getPrice());
            lowestPrice = Math.min(lowestPrice, stock.getPrice());
        }

        Stock topStock = stocks.stream()
                .max(Comparator.comparingDouble(stock -> stock.getPrice() * stock.getQuantity()))
                .orElse(stocks.get(0));

        double averagePrice = stocks.stream()
                .mapToDouble(Stock::getPrice)
                .average()
                .orElse(0.0);

        return new StockStatsResponse(
                stocks.size(),
                totalQuantity,
                Math.round(totalValue * 100.0) / 100.0,
                Math.round(averagePrice * 100.0) / 100.0,
                Math.round(highestPrice * 100.0) / 100.0,
                Math.round(lowestPrice * 100.0) / 100.0,
                topStock != null ? topStock.getSymbol() : "N/A"
        );
    }

    private String normalizeSymbol(String symbol) {
        if (symbol == null) {
            return null;
        }
        return symbol.trim().toUpperCase();
    }

    private String normalizeCompanyName(String companyName) {
        if (companyName == null) {
            return null;
        }
        return companyName.trim();
    }
}

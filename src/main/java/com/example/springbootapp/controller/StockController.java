package com.example.springbootapp.controller;

import com.example.springbootapp.dto.StockRequest;
import com.example.springbootapp.dto.StockStatsResponse;
import com.example.springbootapp.entity.Stock;
import com.example.springbootapp.service.StockService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public Page<Stock> getAllStocks(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return stockService.getStocks(search, page, size, sortBy, direction);
    }

    @GetMapping("/all")
    public List<Stock> getAllStocksList() {
        return stockService.getAllStocks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> getStockById(@PathVariable Long id) {
        return stockService.getStockById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Stock> createStock(@Valid @RequestBody StockRequest request) {
        Stock created = stockService.createStock(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable Long id, @Valid @RequestBody StockRequest request) {
        return stockService.updateStock(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        boolean deleted = stockService.deleteStock(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<StockStatsResponse> getStockStats() {
        return ResponseEntity.ok(stockService.getStockStats());
    }
}

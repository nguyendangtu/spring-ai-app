package com.example.springbootapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class StockRequest {

    @NotBlank(message = "Ticker symbol is required")
    private String symbol;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @PositiveOrZero(message = "Price must be zero or positive")
    private Double price;

    @PositiveOrZero(message = "Quantity must be zero or positive")
    private Integer quantity;

    public StockRequest() {
    }

    public StockRequest(String symbol, String companyName, Double price, Integer quantity) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.price = price;
        this.quantity = quantity;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}


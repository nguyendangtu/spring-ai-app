package com.example.springbootapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "stocks")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ticker symbol is required")
    @Column(nullable = false, unique = true, length = 20)
    private String symbol;

    @NotBlank(message = "Company name is required")
    @Column(nullable = false)
    private String companyName;

    @PositiveOrZero(message = "Price must be zero or positive")
    @Column(nullable = false)
    private Double price;

    @PositiveOrZero(message = "Quantity must be zero or positive")
    @Column(nullable = false)
    private Integer quantity;

    public Stock() {
    }

    public Stock(String symbol, String companyName, Double price, Integer quantity) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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


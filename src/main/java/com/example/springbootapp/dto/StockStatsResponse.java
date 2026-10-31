package com.example.springbootapp.dto;

public class StockStatsResponse {
    private long totalStocks;
    private long totalQuantity;
    private double totalValue;
    private double averagePrice;
    private double highestPrice;
    private double lowestPrice;
    private String topStock;

    public StockStatsResponse() {
    }

    public StockStatsResponse(long totalStocks, long totalQuantity, double totalValue, double averagePrice,
                             double highestPrice, double lowestPrice, String topStock) {
        this.totalStocks = totalStocks;
        this.totalQuantity = totalQuantity;
        this.totalValue = totalValue;
        this.averagePrice = averagePrice;
        this.highestPrice = highestPrice;
        this.lowestPrice = lowestPrice;
        this.topStock = topStock;
    }

    public long getTotalStocks() {
        return totalStocks;
    }

    public void setTotalStocks(long totalStocks) {
        this.totalStocks = totalStocks;
    }

    public long getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public double getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(double highestPrice) {
        this.highestPrice = highestPrice;
    }

    public double getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public String getTopStock() {
        return topStock;
    }

    public void setTopStock(String topStock) {
        this.topStock = topStock;
    }
}


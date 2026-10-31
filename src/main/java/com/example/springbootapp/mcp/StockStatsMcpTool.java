package com.example.springbootapp.mcp;

import com.example.springbootapp.entity.Stock;
import com.example.springbootapp.repository.StockRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StockStatsMcpTool {

    private final StockRepository stockRepository;

    public StockStatsMcpTool(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Tool(description = "Return the first N stocks ordered by ID ascending from the MySQL database.")
    public List<Stock> listFirstStocks(@ToolParam(description = "Number of stocks to return. Default is 10.") Integer limit) {
        int safeLimit = (limit == null || limit <= 0) ? 10 : Math.min(limit, 100);
        return stockRepository.findTopNByOrderByIdAsc(safeLimit);
    }

    @Tool(description = "Return the top N stocks ordered by price descending from the MySQL database.")
    public List<Stock> listTopStocksByPrice(@ToolParam(description = "Number of stocks to return. Default is 5.") Integer limit) {
        int safeLimit = (limit == null || limit <= 0) ? 5 : Math.min(limit, 100);
        return stockRepository.findTopNByOrderByPriceDesc(safeLimit);
    }

    @Tool(description = "Return the stock count and a summary of the current portfolio from the MySQL database.")
    public String getPortfolioSummary() {
        List<Stock> stocks = stockRepository.findAll();
        long totalStocks = stocks.size();
        long totalQuantity = stocks.stream().mapToLong(Stock::getQuantity).sum();
        double totalValue = stocks.stream().mapToDouble(stock -> stock.getPrice() * stock.getQuantity()).sum();
        double avgPrice = stocks.stream().mapToDouble(Stock::getPrice).average().orElse(0.0);
        Stock topStock = stocks.stream().max((a, b) -> Double.compare(a.getPrice(), b.getPrice())).orElse(null);

        return String.format(
                "totalStocks=%d,totalQuantity=%d,totalValue=%.2f,averagePrice=%.2f,topStock=%s",
                totalStocks,
                totalQuantity,
                totalValue,
                avgPrice,
                topStock != null ? topStock.getSymbol() : "N/A"
        );
    }
}

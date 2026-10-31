package com.example.springbootapp.config;

import com.example.springbootapp.entity.Stock;
import com.example.springbootapp.repository.StockRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    @ConditionalOnProperty(name = "app.seed-data", havingValue = "true")
    CommandLineRunner seedStocks(StockRepository stockRepository) {
        return args -> {
            if (stockRepository.count() == 0) {
                stockRepository.save(new Stock("AAPL", "Apple Inc", 214.15, 40));
                stockRepository.save(new Stock("MSFT", "Microsoft", 420.67, 25));
                stockRepository.save(new Stock("GOOG", "Alphabet", 181.30, 55));
                stockRepository.save(new Stock("AMZN", "Amazon", 192.80, 30));
                stockRepository.save(new Stock("NVDA", "NVIDIA", 128.90, 68));
                stockRepository.save(new Stock("META", "Meta Platforms", 512.35, 22));
                stockRepository.save(new Stock("NFLX", "Netflix", 645.10, 15));
                stockRepository.save(new Stock("TSLA", "Tesla", 182.40, 41));
                stockRepository.save(new Stock("ORCL", "Oracle", 112.85, 29));
                stockRepository.save(new Stock("INTC", "Intel", 54.20, 63));
            }
        };
    }
}

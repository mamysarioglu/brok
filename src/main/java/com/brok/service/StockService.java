package com.brok.service;

import com.brok.entity.Stock;
import com.brok.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class StockService {
    private final StockRepository stockRepository;

    public Stock save(Stock stock) {
       return stockRepository.save(stock);
    }
    public List<Stock> findAll() {
        return stockRepository.findAll();
    }
    public Stock findById(UUID id) {
        return stockRepository.findById(id).orElseThrow();
    }
}

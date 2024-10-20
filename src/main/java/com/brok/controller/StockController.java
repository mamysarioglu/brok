package com.brok.controller;

import com.brok.entity.Stock;
import com.brok.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/stock")

public class StockController {

    private final StockService stockService;
    @GetMapping
    ResponseEntity<List<Stock>> getAllStocks() {
        return ResponseEntity.ok(stockService.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    ResponseEntity<Stock> createStock(@RequestBody Stock stock) {
        return ResponseEntity.ok(stockService.save(stock));
    }

    @GetMapping("/{id}")
    ResponseEntity<Stock> getStockById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(stockService.findById(id));
    }
}

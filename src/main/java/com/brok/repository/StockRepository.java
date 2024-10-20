package com.brok.repository;

import com.brok.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StockRepository extends JpaRepository<Stock, UUID> {
}

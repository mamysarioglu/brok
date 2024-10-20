package com.brok.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PendingOrderDTO {
    public PendingOrderDTO(String stockName, UUID stockId, BigDecimal price, long size) {
        this.stockName = stockName;
        this.stockId = stockId;
        this.price = price;
        this.size = size;
    }
    private String stockName;
    private UUID stockId;
    private BigDecimal price;
    private long size;
}

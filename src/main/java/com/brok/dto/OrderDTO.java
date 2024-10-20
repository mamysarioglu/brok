package com.brok.dto;

import com.brok.entity.OrderSide;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDTO {
    @NotNull
    private long size;
    @NotNull
    private BigDecimal price;
    @NotNull
    private OrderSide side;
    @NotBlank
    private String symbol;
}

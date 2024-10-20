package com.brok.dto;

import com.brok.entity.MoneyTransferType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferDTO {
    @NotBlank
    private String iban;
    private BigDecimal amount;
    @NotNull
    private MoneyTransferType transferType;
}

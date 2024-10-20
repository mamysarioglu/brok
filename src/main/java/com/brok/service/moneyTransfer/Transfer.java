package com.brok.service.moneyTransfer;

import java.math.BigDecimal;

public interface Transfer {
    void transfer(String username,String iban, BigDecimal amount);
}

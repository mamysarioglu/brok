package com.brok.service.moneyTransfer;

import com.brok.entity.MoneyTransfer;
import com.brok.entity.MoneyTransferType;
import com.brok.entity.User;
import com.brok.entity.Wallet;
import com.brok.repository.MoneyTransferRepository;
import com.brok.repository.WalletRepository;
import com.brok.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("WITHDRAW")
@RequiredArgsConstructor
public class WithdrawService implements Transfer {
    private final MoneyTransferRepository moneyTransferRepository;
    private final WalletRepository walletRepository;
    private final UserService userService;

    @Override
    public void transfer(String username, String iban, BigDecimal amount) {
        User user = userService.findByUsername(username);
        Wallet wallet = walletRepository.findByUsername(user.getUsername()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (wallet.getAmount().compareTo(amount) >= 0) {
            MoneyTransfer moneyTransfer = MoneyTransfer.builder()
                    .amount(amount)
                    .user(user)
                    .type(MoneyTransferType.WITHDRAW)
                    .IBAN(iban).build();
            wallet.setAmount(wallet.getAmount().subtract(amount));
            walletRepository.save(wallet);
            moneyTransferRepository.save(moneyTransfer);
        } else {
            throw new IllegalArgumentException("Not enough money to withdraw");
        }
    }
}
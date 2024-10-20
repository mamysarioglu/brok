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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service("DEPOSIT")
@RequiredArgsConstructor
public class DepositService implements Transfer {
    private final MoneyTransferRepository moneyTransferRepository;
    private final WalletRepository walletRepository;
    private final UserService userService;

    @Transactional
    @Override
    public void transfer(String username, String iban, BigDecimal amount) {
        User user = userService.findByUsername(username);
        Optional<Wallet> optionalWallet = walletRepository.findByUsername(username);
        Wallet wallet;
        if (optionalWallet.isPresent()) {
            wallet = optionalWallet.get();
            wallet.setAmount(wallet.getAmount().add(amount));
        } else {
            wallet = new Wallet();
            wallet.setAmount(amount);
        }
        wallet.setUser(user);
        walletRepository.save(wallet);
        MoneyTransfer moneyTransfer = MoneyTransfer.builder()
                .amount(amount)
                .user(user)
                .type(MoneyTransferType.DEPOSIT)
                .IBAN(iban).build();
        moneyTransferRepository.save(moneyTransfer);
    }
}
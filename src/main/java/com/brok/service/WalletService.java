package com.brok.service;

import com.brok.entity.Wallet;
import com.brok.exeption.WalletNotFoundException;
import com.brok.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    public Wallet getWallets(String username) {
        return walletRepository.findByUsername(username).orElseThrow( ()->new WalletNotFoundException("No wallet found for "+username));
    }
}

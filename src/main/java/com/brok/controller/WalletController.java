package com.brok.controller;

import com.brok.entity.Wallet;
import com.brok.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/v1/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @GetMapping
    ResponseEntity<Wallet> getAllWallets(Principal principal) {
        return ResponseEntity.ok().body( walletService.getWallets(principal.getName()));
    }
}

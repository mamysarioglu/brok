package com.brok.controller;

import com.brok.dto.TransferDTO;
import com.brok.entity.MoneyTransfer;
import com.brok.service.MoneyTransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1/money-transfer")
@RestController
public class MoneyTransferController {
    private final MoneyTransferService moneyTransferService;

    @PostMapping
    ResponseEntity transfer(@Valid @RequestBody TransferDTO transferDTO, Principal principal) {
        moneyTransferService.transfer(transferDTO,principal.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    ResponseEntity<List<MoneyTransfer>> list(Principal principal) {
        return ResponseEntity.ok(moneyTransferService.list(principal.getName()));
    }
}

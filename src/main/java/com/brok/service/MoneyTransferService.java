package com.brok.service;

import com.brok.dto.TransferDTO;
import com.brok.entity.MoneyTransfer;
import com.brok.repository.MoneyTransferRepository;
import com.brok.repository.UserRepository;
import com.brok.service.moneyTransfer.Transfer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MoneyTransferService {
    private final Map<String, Transfer> transfers;
    private final UserRepository userRepository;
    private final MoneyTransferRepository moneyTransferRepository;
    public void transfer(TransferDTO dto, String username) {
        Transfer transfer = transfers.get(dto.getTransferType().name());
        if (transfer != null) {
            transfer.transfer(username, dto.getIban(), dto.getAmount());
        } else
            throw new IllegalArgumentException("Transfer type not supported");
    }

    public List<MoneyTransfer> list(String username) {

        return moneyTransferRepository.findByUsername(username);
    }
}
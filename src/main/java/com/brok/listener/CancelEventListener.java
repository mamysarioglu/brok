package com.brok.listener;

import com.brok.entity.Asset;
import com.brok.entity.OrderSide;
import com.brok.entity.Orders;
import com.brok.entity.Wallet;
import com.brok.event.CancelEvent;
import com.brok.repository.AssetRepository;
import com.brok.repository.OrderRepository;
import com.brok.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;

@Component
@Slf4j
@RequiredArgsConstructor
public class CancelEventListener {
    private final OrderRepository orderRepository;
    private final WalletRepository walletRepository;
    private final AssetRepository assetRepository;
    @EventListener
    @Async
    @Transactional
    public void onCancelEvent(CancelEvent cancelEvent) {
        log.info("Cancel Event Order Id: {}", cancelEvent.getOrderId());

        Orders order = orderRepository.findById(cancelEvent.getOrderId()).orElseThrow();
        long depositSize = order.getSize()-order.getMatchedSize();
      if(order.getSide() == OrderSide.BUY){
          depositMoney(order,depositSize);
      }else{
          depositAsset(order,depositSize);
      }
    }

    private void depositAsset(Orders order, long depositSize) {
        Asset asset = assetRepository.getAsset(order.getSymbol(),order.getUser().getUsername()).orElseThrow();
        asset.setSize( asset.getSize() + depositSize);
        asset.setBlockSize( asset.getBlockSize() - depositSize);
        assetRepository.save(asset);
    }

    private void depositMoney(Orders order, long depositSize) {
        Wallet wallet = walletRepository.findByUsername(order.getUser().getUsername()).orElseThrow();
        wallet.setAmount(wallet.getAmount().add(order.getPrice().multiply(new BigDecimal(String.valueOf(depositSize)))));
        wallet.setBlockAmount(wallet.getBlockAmount().subtract(order.getPrice().multiply(new BigDecimal(String.valueOf(depositSize)))));
        walletRepository.save(wallet);
    }
}

package com.brok.listener;

import com.brok.dto.OrderProcessDTO;
import com.brok.entity.*;
import com.brok.event.BuyEvent;
import com.brok.event.OrderHistoryEvent;
import com.brok.repository.AssetRepository;
import com.brok.repository.OrderRepository;
import com.brok.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Log4j2
public class BuyEventListener {
    private final OrderRepository orderRepository;
    private final WalletRepository walletRepository;
    private final AssetRepository assetRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @EventListener
    @Transactional
    @Async
    public void onBuyEvent(BuyEvent event) {
        log.info("BuyEvent received : {}", event);
        Orders order = event.getOrder();
        Optional<List<Orders>> canBuys = orderRepository.getForBuy(order.getPrice(), order.getSymbol());
        if (canBuys.isPresent()) {
            List<Orders> orders = canBuys.get();
            List<OrderProcessDTO> processList = new ArrayList<>();
            for (Orders o : orders) {
                long size = o.getSize() - o.getMatchedSize(); //3
                long needSize = order.getSize() - order.getMatchedSize(); //4
                long usedSize;
                if (size <= needSize) {
                    order.setMatchedSize(order.getMatchedSize() + size);
                    o.setMatchedSize(o.getSize());
                    o.setStatus(OrderStatus.MATCHED);
                    usedSize = size;
                } else {
                    order.setMatchedSize(order.getSize());
                    o.setMatchedSize(o.getMatchedSize() + needSize);
                    usedSize = needSize;
                }

                if (order.getMatchedSize() == order.getSize()) {
                    order.setStatus(OrderStatus.MATCHED);
                    processList.add(OrderProcessDTO.builder().fromOrders(o).toOrders(order).size(usedSize).build());
                    break;
                }
                processList.add(OrderProcessDTO.builder().fromOrders(o).toOrders(order).size(usedSize).build());
            }
            if (!processList.isEmpty()) {
                buy(processList);
            }
        }
    }

    public void buy(List<OrderProcessDTO> processDTO) {

        for (OrderProcessDTO dto : processDTO) {

            Orders fromOrders = dto.getFromOrders();
            orderRepository.save(fromOrders);

            User fromUser = fromOrders.getUser();
            Asset asset = fromUser.getAsset().stream().filter(x -> x.getSymbol().equals(fromOrders.getSymbol())).findFirst().orElseThrow();
            asset.setBlockSize(asset.getBlockSize() - dto.getSize());
            assetRepository.save(asset);

            Wallet fromWallet = walletRepository.findByUser(fromUser).orElse(Wallet.builder()
                    .user(fromUser)
                    .amount(BigDecimal.ZERO)
                    .blockAmount(BigDecimal.ZERO)
                    .build());

            fromWallet.setAmount(fromWallet.getAmount().add(fromOrders.getPrice().multiply(new BigDecimal(dto.getSize()))));
            walletRepository.save(fromWallet);
            OrderHistoryEvent event = new OrderHistoryEvent(this,dto.getToOrders().getUser(),fromUser,dto.getFromOrders().getPrice(),dto.getSize(),fromOrders.getSymbol());
            applicationEventPublisher.publishEvent(event);
        }
        processingToUser(processDTO);
    }

    private void processingToUser(List<OrderProcessDTO> processDTO) {
        Orders toOrder = processDTO.get(0).getToOrders();
        orderRepository.save(toOrder);
        User toUser = toOrder.getUser();
        Wallet toWallet = toUser.getWallet();

        BigDecimal totalDifferent = processDTO.stream().map(x->x.getToOrders().getPrice().subtract(x.getFromOrders().getPrice()).multiply(new BigDecimal(x.getSize()))).reduce(BigDecimal.ZERO,BigDecimal::add);//    totalDifferent=totalDifferent.add(toOrders.getPrice().subtract(fromOrders.getPrice()).multiply(new BigDecimal(dto.getSize())));
        toWallet.setAmount(toWallet.getAmount().add(totalDifferent));
        toWallet.setBlockAmount(toWallet.getBlockAmount().subtract(processDTO.stream().map(x->x.getToOrders().getPrice().multiply(new BigDecimal(x.getSize()))).reduce(BigDecimal.ZERO,BigDecimal::add)));
        log.info("Total Different : {}", totalDifferent);
        walletRepository.save(toWallet);

        Asset asset = toUser.getAsset().stream().filter(x -> x.getSymbol().equals(toOrder.getSymbol())).findAny().orElse(Asset.builder()
                .symbol(toOrder.getSymbol())
                .size(0)
                .user(toUser)
                .blockSize(0)
                .build());
        asset.setSize(asset.getSize() + processDTO.stream().map(x->x.getSize()).reduce(0L,Long::sum));
        assetRepository.save(asset);
    }
}
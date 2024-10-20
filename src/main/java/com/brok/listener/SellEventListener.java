package com.brok.listener;

import com.brok.dto.OrderProcessDTO;
import com.brok.entity.*;
import com.brok.event.OrderHistoryEvent;
import com.brok.event.SellEvent;
import com.brok.repository.AssetRepository;
import com.brok.repository.OrderHistoryRepository;
import com.brok.repository.OrderRepository;
import com.brok.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SellEventListener {
    private final OrderRepository orderRepository;
    private final AssetRepository assetRepository;
    private final WalletRepository walletRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @EventListener
    @Transactional
    @Async
    public void onSellEvent(SellEvent event) {
        log.info("Sell event: {}", event);
        Orders order = event.getOrder();
        Optional<List<Orders>> canSell = orderRepository.getForSell(order.getPrice(), order.getSymbol());
        if (canSell.isPresent()) {
            List<Orders> orders = canSell.get();
            List<OrderProcessDTO> processDTOList = new ArrayList<>();
            for (Orders o : orders) {
                long size = o.getSize() -o.getMatchedSize();
                long needSize = order.getSize() - order.getMatchedSize();
                long usedSize;
                if (size <= needSize){
                    order.setMatchedSize(order.getMatchedSize() + size);
                    o.setMatchedSize( size);
                    o.setStatus(OrderStatus.MATCHED);
                    usedSize = size;
                }else{
                    order.setMatchedSize(order.getSize());
                    o.setMatchedSize(o.getMatchedSize() + needSize);
                    usedSize = needSize;
                }
                if (order.getMatchedSize() == order.getSize()){
                    order.setStatus(OrderStatus.MATCHED);
                    processDTOList.add(OrderProcessDTO.builder().fromOrders(order).toOrders(o).size(usedSize).build());
                    break;
                }
                processDTOList.add(OrderProcessDTO.builder().fromOrders(order).toOrders(o).size(usedSize).build());
            }
            if (!processDTOList.isEmpty()){
                sell(processDTOList);
            }
        }
    }

    private void sell(List<OrderProcessDTO> processDTO) {
        for (OrderProcessDTO dto : processDTO) {
            Orders fromOrders = dto.getFromOrders();
            orderRepository.save(fromOrders);

            User fromUser = fromOrders.getUser();
            Asset asset = fromUser.getAsset().stream().filter(x->x.getSymbol().equals(fromOrders.getSymbol())).findFirst().orElseThrow();
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

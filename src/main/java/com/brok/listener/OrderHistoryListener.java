package com.brok.listener;

import com.brok.entity.OrderHistory;
import com.brok.event.OrderHistoryEvent;
import com.brok.repository.OrderHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderHistoryListener {
    private final OrderHistoryRepository orderHistoryRepository;
    @EventListener
    public void saveOrderHistory(OrderHistoryEvent orderHistoryEvent) {
        OrderHistory orderHistory = OrderHistory.builder()
                .fromUser(orderHistoryEvent.getFromUser())
                .toUser(orderHistoryEvent.getToUser())
                .size(orderHistoryEvent.getSize())
                .symbol(orderHistoryEvent.getSymbol())
                .build();
        orderHistoryRepository.save(orderHistory);
    }
}

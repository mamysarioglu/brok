package com.brok.service;

import com.brok.entity.OrderHistory;
import com.brok.entity.OrderSide;
import com.brok.repository.OrderHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderHistoryService {
    private final OrderHistoryRepository orderHistoryRepository;

    public List<OrderHistory> findAll(String username) {
        List<OrderHistory> result =  orderHistoryRepository.getOrders(username);
        result.forEach(orderHistory -> {
          orderHistory.setOrderSide( orderHistory.getFromUser().getUsername().equals(username)
                  ? OrderSide.SELL:OrderSide.BUY);
        });
        return result;
    }
}

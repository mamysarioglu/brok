package com.brok.service;

import com.brok.dto.OrderDTO;
import com.brok.dto.PendingOrderDTO;
import com.brok.entity.OrderSide;
import com.brok.entity.OrderStatus;
import com.brok.entity.Orders;
import com.brok.event.CancelEvent;
import com.brok.repository.OrderRepository;
import com.brok.service.order.OrderProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final Map<String, OrderProcess> orderProcess;
    private final ApplicationEventPublisher applicationEventPublisher;

    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public void save(OrderDTO dto, String username) {
        Orders order = Orders.builder()
                .side(dto.getSide())
                .size(dto.getSize())
                .status(OrderStatus.PENDING)
                .price(dto.getPrice())
                .symbol(dto.getSymbol())
                .build();
        orderProcess.get(order.getSide().name()).process(order, username);
    }

    public List<PendingOrderDTO> getPendingOrders(OrderSide orderSide) {
        return orderRepository.findByStatusAndSide(OrderStatus.PENDING, orderSide);
    }

    public List<Orders> getMyOrders(String username) {
        return orderRepository.getMyOrders(username);
    }


    public void delete(UUID orderId) {
        orderRepository.deleteById(orderId);
        CancelEvent event = new CancelEvent(this, orderId);
        applicationEventPublisher.publishEvent(event);
    }
}
package com.brok.controller;

import com.brok.entity.OrderHistory;
import com.brok.service.OrderHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/order-history")
public class OrderHistoryController {
    private final OrderHistoryService orderHistoryService;

    @GetMapping
    ResponseEntity<List<OrderHistory>> getAllOrderHistory(Principal principal) {
        return ResponseEntity.ok(orderHistoryService.findAll(principal.getName()));
    }
}

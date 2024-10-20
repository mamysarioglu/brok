package com.brok.controller;

import com.brok.dto.OrderDTO;
import com.brok.dto.PendingOrderDTO;
import com.brok.entity.OrderSide;
import com.brok.entity.Orders;
import com.brok.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RequestMapping("/v1/order")
@RestController
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    ResponseEntity<List<Orders>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/my-orders")
    ResponseEntity< List<Orders>> getMyOrder(Principal principal) {
        return ResponseEntity.ok(orderService.getMyOrders(principal.getName()));
    }

    @GetMapping("/pending/{orderSide}")
    ResponseEntity<List<PendingOrderDTO>> getOrderById(@PathVariable("orderSide") OrderSide orderSide) {
        return ResponseEntity.ok(orderService.getPendingOrders(orderSide));
    }

    @PostMapping
    ResponseEntity createOrder(@Valid @RequestBody OrderDTO order, Principal principal) {
        orderService.save(order,principal.getName());
        return ResponseEntity.ok("success");
    }

    @DeleteMapping("/{orderId}")
    ResponseEntity deleteOrderById(@PathVariable("orderId") UUID orderId) {
        orderService.delete(orderId);
        return ResponseEntity.ok("success");
    }
}

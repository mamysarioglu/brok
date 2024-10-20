package com.brok.event;

import org.springframework.context.ApplicationEvent;

import java.util.UUID;

public class CancelEvent extends ApplicationEvent {
    private UUID orderId;
    public CancelEvent(Object source, UUID orderId) {
        super(source);
        this.orderId = orderId;
    }
    public UUID getOrderId() {
        return orderId;
    }
}

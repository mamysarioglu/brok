package com.brok.event;

import com.brok.entity.Orders;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

public class SellEvent extends ApplicationEvent {
    private Orders order;
    private long size;
    private BigDecimal price;
    public SellEvent(Object source, Orders order) {
        super(source);
        this.order = order;
    }
    public Orders getOrder() {
        return order;
    }
    public void setOrder(Orders order) {
        this.order = order;
    }
    public long getSize() {
        return size;
    }
    public void setSize(long size) {
        this.size = size;
    }
    public BigDecimal getPrice() {
        return price;
    }
}

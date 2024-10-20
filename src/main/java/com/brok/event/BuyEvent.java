package com.brok.event;

import com.brok.entity.Orders;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

public class BuyEvent extends ApplicationEvent {
    private Orders order;
    private BigDecimal price;
    private long size;

    public BuyEvent(Object source, Orders order) {
        super(source);
        this.order = order;
    }
    public Orders getOrder() {
        return order;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setSize(long size) {
        this.size = size;
    }
    public long getSize() {
        return size;
    }
}

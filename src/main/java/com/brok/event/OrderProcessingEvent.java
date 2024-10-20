package com.brok.event;

import com.brok.entity.Orders;
import com.brok.entity.User;
import jakarta.persistence.criteria.Order;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

public class OrderProcessingEvent extends ApplicationEvent {
    private Orders fromOrder;
    private Orders toOrder;
    private long size;

    public OrderProcessingEvent(Object source, Orders fromOrder, Orders toOrder, long size) {
        super(source);
        this.fromOrder = fromOrder;
        this.toOrder = toOrder;
        this.size = size;
    }


    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Orders getFromOrder() {
        return fromOrder;
    }

    public void setFromOrder(Orders fromOrder) {
        this.fromOrder = fromOrder;
    }

    public Orders getToOrder() {
        return toOrder;
    }

    public void setToOrder(Orders toOrder) {
        this.toOrder = toOrder;
    }
}

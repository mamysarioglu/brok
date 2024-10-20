package com.brok.event;

import com.brok.entity.User;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

public class OrderHistoryEvent extends ApplicationEvent {
    private User toUser;
    private User fromUser;
    private BigDecimal price;
    private long size;
    private String symbol;
    public OrderHistoryEvent(Object source, User toUser, User fromUser, BigDecimal price, long size, String symbol) {
        super(source);
        this.toUser = toUser;
        this.fromUser = fromUser;
        this.price = price;
        this.size = size;
        this.symbol = symbol;
    }
    public User getToUser() {
        return toUser;
    }
    public User getFromUser() {
        return fromUser;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public long getSize() {
        return size;
    }
    public String getSymbol() {
        return symbol;
    }
}

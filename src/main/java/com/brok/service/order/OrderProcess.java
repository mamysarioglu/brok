package com.brok.service.order;


import com.brok.entity.Orders;

public interface OrderProcess {
    void process(Orders order, String username);
}

package com.brok.dto;

import com.brok.entity.Orders;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderProcessDTO {private Orders fromOrders; private Orders toOrders;private long size;

}
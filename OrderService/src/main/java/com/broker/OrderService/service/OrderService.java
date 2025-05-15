package com.broker.OrderService.service;


import com.broker.OrderService.DTO.OrderRequest;
import com.broker.OrderService.DTO.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(OrderRequest request);
    List<OrderResponse> getAllOrders();
}

package com.broker.OrderService.mapper;

import com.broker.OrderService.DTO.OrderRequest;
import com.broker.OrderService.DTO.OrderResponse;
import com.broker.OrderService.Entity.Order;

import java.time.Instant;

public class OrderMapper {
    public static Order toEntity(OrderRequest request, Instant timestamp) {
        Order order = new Order();
        order.setEmail(request.getEmail());
        order.setStockSymbol(request.getStockSymbol());
        order.setQuantity(request.getQuantity());
        order.setType(request.getType());
        order.setPrice(request.getPrice());
        order.setTimestamp(timestamp);
        return order;
    }

    public static OrderResponse toResponse(Order order) {
        return new OrderResponse(order.getOrderId(),order.getEmail(),order.getStockSymbol(),
                order.getQuantity(), order.getPrice(), order.getType(),order.getTimestamp());
    }
}

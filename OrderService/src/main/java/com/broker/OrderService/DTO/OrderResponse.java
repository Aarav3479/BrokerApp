package com.broker.OrderService.DTO;

import com.broker.OrderService.Entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long orderId;
    private Long userId;
    private String stockSymbol;
    private int quantity;
    private double price;
    private Order.OrderType type;
    private Instant timestamp;
}

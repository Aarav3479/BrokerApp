package com.broker.OrderService.DTO;

import com.broker.OrderService.Entity.Order;
import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPlacedEvent {
    private Long orderId;
    private Long userId;
    private String stockSymbol;
    private int quantity;
    private double price;
    private Order.OrderType type;
    private Instant timestamp;
}

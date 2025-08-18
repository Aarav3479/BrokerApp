package com.broker.OrderService.DTO;

import com.broker.OrderService.Entity.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPlacedEvent {
    private Long orderId;
    private String email;
    private String stockSymbol;
    private int quantity;
    private double price;
    private Order.OrderType type;
    private Instant orderTimestamp;
}

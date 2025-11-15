package com.broker.OrderService.DTO;

import com.broker.OrderService.Entity.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long orderId;
    private String email;
    private String stockSymbol;
    private Integer quantity;
    private double price;
    private Order.OrderType type;
    private Instant timestamp;
}

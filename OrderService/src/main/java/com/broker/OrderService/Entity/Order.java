package com.broker.OrderService.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String stockSymbol;
    private int quantity;
    private double price;

    @Enumerated(EnumType.STRING)
    private OrderType type;

    private Instant timestamp;

    public enum OrderType {
        BUY, SELL
    }
}

package com.broker.portfolio.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradePlacedEvent {
    private Long orderId;
    private String email;
    private String stockSymbol;
    private int quantity;
    private double price;
    private OrderType type;
    private Instant tradeTimestamp;
    private Instant orderTimestamp;
    public enum OrderType {
        BUY, SELL
    }
}

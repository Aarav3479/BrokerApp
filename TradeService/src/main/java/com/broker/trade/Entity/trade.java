package com.broker.trade.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tradeId;
    private Long orderId;
    private String email;
    private String stockSymbol;
    private int quantity;
    private double price;
    private Instant orderTimestamp;
    private Instant tradeTimestamp;
    public enum OrderType {
        BUY, SELL
    }
}

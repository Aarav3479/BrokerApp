package com.broker.trade.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trade {
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
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    public enum OrderType {
        BUY, SELL
    }
}

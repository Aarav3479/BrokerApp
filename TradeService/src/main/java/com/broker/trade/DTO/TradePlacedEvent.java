package com.broker.trade.DTO;

import com.broker.trade.Entity.trade;
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
    private Instant tradeTimestamp;
    private Instant orderTimestamp;

}

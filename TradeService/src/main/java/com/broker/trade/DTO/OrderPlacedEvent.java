package com.broker.trade.DTO;

import com.broker.trade.Entity.Trade;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Trade.OrderType type;
    private Instant orderTimestamp;
}

package com.broker.trade.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradePair {
    private TradePlacedEvent buyEvent;
    private TradePlacedEvent sellEvent;
}

package com.broker.trade.Entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TradeKey implements Serializable {
    private long tradeId;
    private long orderId;
}
